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
class CategoriesMenuItemType implements MenuItemType {

	private static final String CATEGORIES_PATH = "/store/categories/"

    @Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Override
    String getId() {
		return "productCategories"
    }

	@Override
    String getName() {
		return "Products Categories menu item"
    }

	@Override
    String getDescription() {
		return "A category list items"
    }

	@Override
    void setupMenuItem(MenuContext context) {

		List<tools.dynamia.cms.products.domain.ProductCategory> categories = service.getCategories(context.menuItem.menu.site)

        MenuItem item = context.menuItem.clone()
        item.name = context.menuItem.name
        item.title = context.menuItem.title
        item.order = context.menuItem.order


        for (tools.dynamia.cms.products.domain.ProductCategory cat : categories) {
			MenuItem catMenuItem = new MenuItem(getCategoryName(cat),
					CATEGORIES_PATH + cat.id + "/" + cat.alias)
            item.addMenuItem(catMenuItem)

            List<tools.dynamia.cms.products.domain.ProductCategory> subcategories = service.getSubcategories(cat)

            for (tools.dynamia.cms.products.domain.ProductCategory subcat : subcategories) {
				MenuItem subcatMenuItem = new MenuItem(getCategoryName(subcat),
						CATEGORIES_PATH + subcat.id + "/" + subcat.alias)
                catMenuItem.addMenuItem(subcatMenuItem)
            }

		}

		context.update(item)

    }

	private String getCategoryName(tools.dynamia.cms.products.domain.ProductCategory category) {
		String name = category.name
        if (category.alternateName != null && !category.alternateName.trim().empty) {
			name = category.alternateName
        }
		return name
    }

	private String clean(String name) {
		name = name.replace("-", " ")
        name = name.trim()

        return name
    }

}
