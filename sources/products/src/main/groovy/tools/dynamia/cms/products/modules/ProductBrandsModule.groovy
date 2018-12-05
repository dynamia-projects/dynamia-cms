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
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductBrandsModule extends AbstractModule {

	@Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
	private CrudService crudService

    ProductBrandsModule() {
		super("products_brands", "Products Brands", "products/modules/brandslist")
        description = "Show a products brands list"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "20-04-2017")
        variablesNames = "brands"

    }

	@Override
    void init(ModuleContext context) {
		List<ProductBrand> brands = null

        if (brands == null || brands.empty) {
			brands = service.getBrands(context.site)
        }

        context.moduleInstance.addObject("brands", brands)

    }

}
