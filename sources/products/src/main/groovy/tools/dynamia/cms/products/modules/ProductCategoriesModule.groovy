/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
