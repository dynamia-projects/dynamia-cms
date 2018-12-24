/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteCache
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.ProductCategoryGroup
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.ProductsUtil
import tools.dynamia.cms.products.domain.*
import tools.dynamia.cms.products.services.ProductsService
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

        finalDetails.each { filter ->
            filter.currentValues.clear()
            products.each { p ->
                p.details.each { pd ->
                    if (pd.name.equalsIgnoreCase(filter.name) && !filter.currentValues.contains(pd.value) && pd.value) {
                        filter.currentValues << pd.value
                    }
                }
            }
        }



        if (filteredDetails != null) {

            filteredDetails.forEach { k, v ->
                ProductCategoryDetail det = finalDetails.stream().filter { d -> d.name.equalsIgnoreCase(k) }.findFirst().orElse(null)
                if (det != null) {
                    det.selected = true
                    det.selectedValue = v
                }
            }
        }

        def emptyFilters = finalDetails.findAll { it.currentValues.empty && !it.selected }
        if (emptyFilters) {
            finalDetails.removeAll(emptyFilters)
        }


        mv.addObject("prd_groups", ProductCategoryGroup.groupByCategory(products))
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
        ProductsUtil.setupProductsVar(products, mv)
    }

}
