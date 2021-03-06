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
import tools.dynamia.cms.menus.domain.MenuItemGroup
import tools.dynamia.cms.menus.domain.MenuItemParameter
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageParameter

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class SubcategoriesMenuItemType implements MenuItemType {

	private static final String PARAM_CATEGORY = "category"

    private static final String CATEGORIES_PATH = "/store/categories/"

    @Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Override
    String getId() {
		return "subcategories"
    }

	@Override
    String getName() {
		return "Sub categories menu item"
    }

	@Override
    String getDescription() {
		return "A sub category list items, you should specified a category id using item parameters"
    }

	@Override
    void setupMenuItem(MenuContext context) {

		String categoryParamValue = tryGetPageCategory(context.menuItem.page)
        if (categoryParamValue == null) {
			MenuItemParameter parameter = context.getParameter(PARAM_CATEGORY)
            if (parameter != null) {
				categoryParamValue = parameter.value
            }
		}

		if (categoryParamValue != null) {
			Long categoryId = new Long(categoryParamValue)
            tools.dynamia.cms.products.domain.ProductCategory category = service.getCategoryById(categoryId)
            if (category != null) {
				List<tools.dynamia.cms.products.domain.ProductCategory> subcategories = service.getSubcategories(category)
                MenuItem item = context.menuItem.clone()
                item.name = context.menuItem.name
                item.title = context.menuItem.title
                item.order = context.menuItem.order

                item.subtitle = getCategoryName(category)
                if (item.href == null) {
                    item.href = CATEGORIES_PATH + category.id + "/" + category.alias
                }

                item.subitems.clear()
                if (subcategories != null) {
					for (tools.dynamia.cms.products.domain.ProductCategory subcat : subcategories) {
						item.addMenuItem(new MenuItem(clean(getCategoryName(subcat)), CATEGORIES_PATH + subcat.id + "/"
								+ subcat.alias))
                    }
				}

                item.itemsGroups.clear()
                List<tools.dynamia.cms.products.domain.ProductCategory> relatedCategories = service.getRelatedCategories(category)
                if (relatedCategories != null && !relatedCategories.empty) {
					for (tools.dynamia.cms.products.domain.ProductCategory relatedCategory : relatedCategories) {
						String groupName = getCategoryName(relatedCategory)

                        MenuItemGroup group = new MenuItemGroup(groupName)
                        group.href = CATEGORIES_PATH + relatedCategory.id + "/" + relatedCategory.alias

                        List<tools.dynamia.cms.products.domain.ProductCategory> relatedSubcategories = service.getSubcategories(relatedCategory)
                        if (relatedSubcategories != null) {
							for (tools.dynamia.cms.products.domain.ProductCategory relSubcat : relatedSubcategories) {
								group.addMenuItem(new MenuItem(clean(relSubcat.name), CATEGORIES_PATH + relSubcat.id + "/"
										+ relSubcat.alias))
                            }
						}
						if (!group.subitems.empty) {
							item.addMenuItemGroup(group)
                        }
					}
				}
				context.update(item)
            }
		}

	}

	private String tryGetPageCategory(Page page) {
		String value = null
        ProductsPageType productsPageType = new ProductsPageType()
        if (page != null && page.type.equals(productsPageType.id)) {
			PageParameter param = page.getParam(PARAM_CATEGORY)
            if (param != null) {
				value = param.value
            }
		}

		return value
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
