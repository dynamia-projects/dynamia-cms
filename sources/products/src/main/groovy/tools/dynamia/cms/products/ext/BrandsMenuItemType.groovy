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
package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.menus.MenuContext
import tools.dynamia.cms.menus.api.MenuItemType
import tools.dynamia.cms.menus.domain.MenuItem

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class BrandsMenuItemType implements MenuItemType {

	private static final String BRANDS_PATH = "/store/brands/"

    @Autowired
	private tools.dynamia.cms.products.services.ProductsService service

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

		List<tools.dynamia.cms.products.domain.ProductBrand> brands = service.getBrands(context.menuItem.menu.site)

        MenuItem item = context.menuItem.clone()
        item.name = context.menuItem.name
        item.title = context.menuItem.title
        item.order = context.menuItem.order

        for (tools.dynamia.cms.products.domain.ProductBrand brd : brands) {
			MenuItem brandItem = new MenuItem(brd.name, BRANDS_PATH + brd.alias)
            item.addMenuItem(brandItem)

        }

		context.update(item)

    }

}
