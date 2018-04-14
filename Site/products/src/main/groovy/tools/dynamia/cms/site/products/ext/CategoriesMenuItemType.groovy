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
import tools.dynamia.cms.site.products.domain.ProductCategory
import tools.dynamia.cms.site.products.services.ProductsService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class CategoriesMenuItemType implements MenuItemType {

	private static final String CATEGORIES_PATH = "/store/categories/"

    @Autowired
	private ProductsService service

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

		List<ProductCategory> categories = service.getCategories(context.menuItem.menu.site)

        MenuItem item = context.menuItem.clone()
        item.name = context.menuItem.name
        item.title = context.menuItem.title
        item.order = context.menuItem.order


        for (ProductCategory cat : categories) {
			MenuItem catMenuItem = new MenuItem(getCategoryName(cat),
					CATEGORIES_PATH + cat.id + "/" + cat.alias)
            item.addMenuItem(catMenuItem)

            List<ProductCategory> subcategories = service.getSubcategories(cat)

            for (ProductCategory subcat : subcategories) {
				MenuItem subcatMenuItem = new MenuItem(getCategoryName(subcat),
						CATEGORIES_PATH + subcat.id + "/" + subcat.alias)
                catMenuItem.addMenuItem(subcatMenuItem)
            }

		}

		context.update(item)

    }

	private String getCategoryName(ProductCategory category) {
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
