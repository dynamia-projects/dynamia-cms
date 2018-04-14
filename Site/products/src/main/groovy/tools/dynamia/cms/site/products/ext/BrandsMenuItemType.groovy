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
package tools.dynamia.cms.site.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.CMSExtension
import tools.dynamia.cms.site.menus.MenuContext
import tools.dynamia.cms.site.menus.api.MenuItemType
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.cms.site.products.domain.ProductBrand
import tools.dynamia.cms.site.products.services.ProductsService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class BrandsMenuItemType implements MenuItemType {

	private static final String BRANDS_PATH = "/store/brands/"

    @Autowired
	private ProductsService service

    @Override
    String getId() {
		return "productBrands"
    }

	@Override
    String getName() {
		return "Products Brands menu item"
    }

	@Override
    String getDescription() {
		return "A brands list items"
    }

	@Override
    void setupMenuItem(MenuContext context) {

		List<ProductBrand> brands = service.getBrands(context.getMenuItem().getMenu().getSite())

        MenuItem item = context.getMenuItem().clone()
        item.setName(context.getMenuItem().getName())
        item.setTitle(context.getMenuItem().getTitle())
        item.setOrder(context.getMenuItem().getOrder())

        for (ProductBrand brd : brands) {
			MenuItem brandItem = new MenuItem(brd.getName(), BRANDS_PATH + brd.getAlias())
            item.addMenuItem(brandItem)

        }

		context.update(item)

    }

}
