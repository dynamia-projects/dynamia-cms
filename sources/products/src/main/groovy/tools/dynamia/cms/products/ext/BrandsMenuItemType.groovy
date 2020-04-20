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
