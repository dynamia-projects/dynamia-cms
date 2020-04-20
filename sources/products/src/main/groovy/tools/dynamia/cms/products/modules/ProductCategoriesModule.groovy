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
import tools.dynamia.cms.core.domain.ModuleInstanceParameter
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductCategoriesModule extends AbstractModule {

    @Autowired
    private ProductsService service

    @Autowired
    private CrudService crudService

    ProductCategoriesModule() {
        super("products_categories", "Products Categories", "products/modules/categorylist")
        description = "Show a products categories list"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "25-07-2016")
        setVariablesNames("categories")

    }

    @Override
    void init(ModuleContext context) {
        List<ProductCategory> categories = null

        ModuleInstanceParameter brandId = context.getParameter("brand")
        if (brandId != null) {
            ProductBrand brand = crudService.find(ProductBrand.class, new Long(brandId.value))
            if (brand != null) {
                categories = service.getCategories(brand)
            }
        }

        try {
            long parentCategoryId = Long.parseLong(context.getParameterValue("parentCategory", "0"))
            if (parentCategoryId > 0) {
                ProductCategory parentCategory = crudService.find(ProductCategory.class, parentCategoryId)
                categories = service.getSubcategories(parentCategory)
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        if (categories == null || categories.empty) {
            categories = service.getCategories(context.site)
        }

        context.moduleInstance.addObject("categories", categories)

    }

}
