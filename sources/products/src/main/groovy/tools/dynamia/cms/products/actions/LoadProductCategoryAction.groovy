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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteCache
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.domain.ProductCategoryDetail
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.domain.query.BooleanOp
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

/**
 * @author Mario Serrano Leones
 */
@CMSAction
class LoadProductCategoryAction implements SiteAction {

    public static final String NAME = "loadProductCategory"
    private static final List<String> ORDER_FIELDS = Collections
            .unmodifiableList(Arrays.asList("name", "price", "brand.name", "views"))

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

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
        ModelAndView mv = evt.modelAndView

        loadProductsFromCategory(evt, mv)


    }

    private void loadProductsFromCategory(ActionEvent evt, ModelAndView mv) {
        String orderfield = evt.request.getParameter("order")
        boolean asc = "1".equals(evt.request.getParameter("asc"))

        if (orderfield == null || !ORDER_FIELDS.contains(orderfield)) {
            orderfield = "price"
        }

        ProductsSiteConfig config = service.getSiteConfig(evt.site)
        ProductCategory category = null
        if (evt.data instanceof String) {
            String alias = (String) evt.data
            category = service.getCategoryByAlias(evt.site, alias)
        } else {
            Long id = (Long) evt.data
            category = crudService.find(ProductCategory.class, id)
        }

        List<Product> products = null
        QueryParameters qp = QueryParameters.with("active", true).add("site", evt.site)

        if (category.parent == null) {
            qp.addGroup(QueryParameters.with("category.parent", QueryConditions.eq(category, BooleanOp.OR))
                    .add("category", QueryConditions.eq(category, BooleanOp.OR)), BooleanOp.AND)
        } else {
            qp.add("category", category)
        }

        qp.orderBy(orderfield, asc)

        if (config != null) {
            qp.paginate(config.productsPerPage)
        }

        String title = category.name
        Map<String, String> filteredDetails = null
        if (evt.request.parameterMap.containsKey("featured")) {
            qp.add("featured", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.request.parameterMap.containsKey("sale")) {
            qp.add("sale", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.request.parameterMap.containsKey("new")) {
            qp.add("newproduct", true)
            products = crudService.find(Product.class, qp)
        } else if (evt.request.parameterMap.containsKey("views")) {
            qp.orderBy("views", false)
            products = crudService.find(Product.class, qp)
        } else if (evt.request.getParameter("q") != null) {
            ProductSearchForm form = new ProductSearchForm()
            form.categoryId = category.id
            form.detail = evt.request.getParameter("q")
            form.detail2 = evt.request.getParameter("q2")
            form.detail3 = evt.request.getParameter("q3")
            form.detail4 = evt.request.getParameter("q4")
            form.maxPrice = CMSUtil.toBigDecimal(evt.request.getParameter("max"))
            form.minPrice = CMSUtil.toBigDecimal(evt.request.getParameter("min"))
            try {
                ProductBrand brand = service.getBrandByAlias(evt.site, evt.request.getParameter("brand"))
                form.brandId = brand.id
                title = title + " " + brand.name
            } catch (Exception e) {
            }
            form.order = null
            if (form.detail != null) {
                title = title + " " + form.detail.replaceAll("=", " ")
            }
            products = service.filterProducts(evt.site, form)
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
                ProductCategoryDetail det = finalDetails.stream().filter { d -> d.name.equalsIgnoreCase(k) }.findFirst().orElse(null)
                if (det != null) {
                    det.selected = true
                    det.selectedValue = v
                }
            }
        }


        mv.addObject("prd_groups", tools.dynamia.cms.products.ProductCategoryGroup.groupByCategory(products))
        mv.addObject("prd_category", category)
        mv.addObject("prd_category_details", finalDetails)
        mv.addObject("prd_subcategories", subcategories)
        mv.addObject("prd_categoryBrands", categoryBrands)
        mv.addObject("prd_parentCategory", category.parent)
        mv.addObject("orderfield", orderfield)
        mv.addObject("asc", asc)
        // mv.addObject("prd_specialProducts", specialProducts);

        if (mv.model.get("filteredDetails") == null) {
            mv.addObject("filteredDetails", Collections.EMPTY_LIST)
        }
        products = CMSUtil.setupPagination(products, evt.request, mv)
        tools.dynamia.cms.products.ProductsUtil.setupProductsVar(products, mv)
    }

}
