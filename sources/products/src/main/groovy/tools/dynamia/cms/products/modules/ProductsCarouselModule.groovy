/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.commons.collect.PagedList
import tools.dynamia.domain.query.BooleanOp
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

/**
 * Created by Mario on 18/11/2014.
 */
@CMSModule
class ProductsCarouselModule extends AbstractModule {

    private static final String PARAM_PAGE_SIZE = "pageSize"

    private static final String PARAM_ORDER_BY = "orderBy"

    private static final String PARAM_STATUS = "status"

    private static final String PARAM_STATUS_2 = "status2"

    private static final String PARAM_CATEGORY = "category"

    private static final String PARAM_COLUMNS = "columns"

    private static final String PARAM_TYPE = "type"

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    ProductsCarouselModule() {
        super("products_carousel", "Products Carousel", "products/modules/carousel")
        description = "Show a products list carousel"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "18-11-2014")
        putMetadata(PARAM_COLUMNS, "4")
        putMetadata(PARAM_TYPE, "featured")
        putMetadata(PARAM_PAGE_SIZE, "16")
        setVariablesNames("products,", PARAM_COLUMNS)
    }

    @Override
    void init(ModuleContext context) {
        ProductCarouselType type = ProductCarouselType.valueOf(getParameter(context, PARAM_TYPE).toUpperCase())
        int columns
        try {
            columns = Integer.parseInt(getParameter(context, PARAM_COLUMNS))
        } catch (NumberFormatException e1) {
            columns = Integer.parseInt((String) metadata.get(PARAM_COLUMNS))
        }

        String category = getParameter(context, PARAM_CATEGORY)
        String status = getParameter(context, PARAM_STATUS)
        String status2 = getParameter(context, PARAM_STATUS_2)
        String orderBy = null
        try {
            ProductOrderField orderField = ProductOrderField
                    .valueOf(getParameter(context, PARAM_ORDER_BY).toUpperCase())
            orderBy = orderField.field
        } catch (Exception e) {
            // TODO: handle exception
        }

        String pageSizeParam = getParameter(context, PARAM_PAGE_SIZE)

        List<Product> products = new ArrayList<>()
        QueryParameters params = QueryParameters.with("site", context.site).add("active", true)
                .paginate(columns * 4)

        if (pageSizeParam != null) {
            try {
                int pageSize = Integer.parseInt(pageSizeParam)
                params.paginate(pageSize)
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        switch (type) {
            case ProductCarouselType.ALL:
                products = service.getSpecialProducts(context.site)
                break
            case ProductCarouselType.FEATURED:
                params.add("featured", true)
                break
            case ProductCarouselType.NEW:
                params.add("newproduct", true)
                break
            case ProductCarouselType.SALE:
                params.add("sale", true)
                break
            case ProductCarouselType.CORPORATE:
                params.add("corporate",true)
                break
            case ProductCarouselType.SPECIAL:
                params.add("special",true)
                break
            case ProductCarouselType.MOST_VIEWED:
                params.orderBy("views", false)
                break
            case ProductCarouselType.CUSTOM:
                params.add(PARAM_STATUS, status)
                if (status2) {
                    params.add(PARAM_STATUS_2, status2)
                }
                break
        }

        if (orderBy != null) {
            params.orderBy(orderBy, true)
        }

        if (category != null && !category.empty) {
            try {
                Long categoryId = new Long(category)
                QueryParameters catParams = QueryParameters
                        .with("category.id", QueryConditions.eq(categoryId, BooleanOp.OR))
                        .add("category.parent.id", QueryConditions.eq(categoryId, BooleanOp.OR))
                params.addGroup(catParams, BooleanOp.AND)
            } catch (NumberFormatException nf) {

            }
        }

        if (products.empty) {
            products = crudService.find(Product.class, params)
        }

        if (products instanceof PagedList) {
            PagedList<Product> pagedList = (PagedList<Product>) products
            products = pagedList.dataSource.pageData
        }

        ModuleInstance mod = context.moduleInstance
        mod.addObject("products", products)
        mod.addObject(PARAM_COLUMNS, columns)
    }

    private String getParameter(ModuleContext context, String name) {
        String param = context.moduleInstance.getParameterValue(name)
        if (param == null || param.trim().empty) {
            Object metadata = metadata.get(name)
            if (metadata != null) {
                param = metadata.toString()
            }
        }

        if (param != null) {
            param = param.trim()
        }

        return param
    }

}
