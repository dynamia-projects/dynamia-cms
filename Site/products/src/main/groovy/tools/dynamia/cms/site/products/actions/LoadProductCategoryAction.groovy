/*
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteCache
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.products.ProductCategoryGroup
import tools.dynamia.cms.site.products.ProductSearchForm
import tools.dynamia.cms.site.products.ProductsUtil
import tools.dynamia.cms.site.products.domain.*
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.query.BooleanOp
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

import java.util.Arrays
import java.util.Collections
import java.util.List
import java.util.Map
import java.util.stream.Collectors

/**
 * @author Mario Serrano Leones
 */
@CMSAction
class LoadProductCategoryAction implements SiteAction {

    public static final String NAME = "loadProductCategory"
    private static final List<String> ORDER_FIELDS = Collections
            .unmodifiableList(Arrays.asList("name", "price", "brand.name", "views"))

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    @Autowired
    private SiteCache siteCache

    @Override
    String getName() {
        return NAME
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView()

        loadProductsFromCategory(evt, mv)


    }

    private void loadProductsFromCategory(ActionEvent evt, ModelAndView mv) {
        String orderfield = evt.getRequest().getParameter("order")
        boolean asc = "1".equals(evt.getRequest().getParameter("asc"))

        if (orderfield == null || !ORDER_FIELDS.contains(orderfield)) {
            orderfield = "price"
        }

        ProductsSiteConfig config = service.getSiteConfig(evt.getSite())
        ProductCategory category = null
        if (evt.getData() instanceof String) {
            String alias = (String) evt.getData()
            category = service.getCategoryByAlias(evt.getSite(), alias)
        } else {
            Long id = (Long) evt.getData()
            category = crudService.find(ProductCategory.class, id)
        }

        List<Product> products = null
        QueryParameters qp = QueryParameters.with("active", true).add("site", evt.getSite())

        if (category.getParent() == null) {
            qp.addGroup(QueryParameters.with("category.parent", QueryConditions.eq(category, BooleanOp.OR))
                    .add("category", QueryConditions.eq(category, BooleanOp.OR)), BooleanOp.AND)
        } else {
            qp.add("category", category)
        }

        qp.orderBy(orderfield, asc)

        if (config != null) {
            qp.paginate(config.getProductsPerPage())
        }

        String title = category.getName()
        Map<String, String> filteredDetails = null
        if (evt.getRequest().getParameterMap().containsKey("featured")) {
            qp.add("featured", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.getRequest().getParameterMap().containsKey("sale")) {
            qp.add("sale", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.getRequest().getParameterMap().containsKey("new")) {
            qp.add("newproduct", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.getRequest().getParameterMap().containsKey("views")) {
            qp.orderBy("views", false)
            products = crudService.find(Product.class, qp)
        } else if (evt.getRequest().getParameter("q") != null) {
            ProductSearchForm form = new ProductSearchForm()
            form.setCategoryId(category.getId())
            form.setDetail(evt.getRequest().getParameter("q"))
            form.setDetail2(evt.getRequest().getParameter("q2"))
            form.setDetail3(evt.getRequest().getParameter("q3"))
            form.setDetail4(evt.getRequest().getParameter("q4"))
            form.setMaxPrice(CMSUtil.toBigDecimal(evt.getRequest().getParameter("max")))
            form.setMinPrice(CMSUtil.toBigDecimal(evt.getRequest().getParameter("min")))
            try {
                ProductBrand brand = service.getBrandByAlias(evt.getSite(), evt.getRequest().getParameter("brand"))
                form.setBrandId(brand.getId())
                title = title + " " + brand.getName()
            } catch (Exception e) {
            }
            form.setOrder(null)
            if (form.getDetail() != null) {
                title = title + " " + form.getDetail().replaceAll("=", " ")
            }
            products = service.filterProducts(evt.getSite(), form)
            filteredDetails = (Map<String, String>) form.getAttribute("filteredDetails")

        } else {
            products = service.getProducts(category, orderfield, asc)
        }
        // List<Product> specialProducts = service.getSpecialProducts(category);
        List<ProductCategory> subcategories = service.getSubcategories(category)
        List<ProductBrand> categoryBrands = service.getBrands(category)

        mv.addObject("title", title)

        List<ProductCategoryDetail> details = service.getCategoryDetails(category)
        List<ProductCategoryDetail> finalDetails = details.collect { it.clone() }

        if (filteredDetails != null) {

            filteredDetails.forEach { k, v ->
                ProductCategoryDetail det = finalDetails.stream().filter { d -> d.getName().equalsIgnoreCase(k) }.findFirst().orElse(null)
                if (det != null) {
                    det.setSelected(true)
                    det.setSelectedValue(v)
                }
            }
        }


        mv.addObject("prd_groups", ProductCategoryGroup.groupByCategory(products))
        mv.addObject("prd_category", category)
        mv.addObject("prd_category_details", finalDetails)
        mv.addObject("prd_subcategories", subcategories)
        mv.addObject("prd_categoryBrands", categoryBrands)
        mv.addObject("prd_parentCategory", category.getParent())
        mv.addObject("orderfield", orderfield)
        mv.addObject("asc", asc)
        // mv.addObject("prd_specialProducts", specialProducts);

        if (mv.getModel().get("filteredDetails") == null) {
            mv.addObject("filteredDetails", Collections.EMPTY_LIST)
        }
        products = CMSUtil.setupPagination(products, evt.getRequest(), mv)
        ProductsUtil.setupProductsVar(products, mv)
    }

}
