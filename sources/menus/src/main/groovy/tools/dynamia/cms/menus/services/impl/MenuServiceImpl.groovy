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
package tools.dynamia.cms.menus.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.menus.MenuContext
import tools.dynamia.cms.menus.api.MenuItemType
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.cms.menus.services.MenuService
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@Service
@CompileStatic
class MenuServiceImpl implements MenuService {

	@Autowired
	private CrudService crudService

	@Override
	@Cacheable(value = "menus", key = "#site.key")
	Menu getMainMenu(Site site) {
		QueryParameters qp = QueryParameters.with("site", site)
		return crudService.findSingle(Menu.class, qp)
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#alias")
	Menu getMenu(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("site", site).add("alias", alias)
		return crudService.findSingle(Menu.class, qp)
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#id")
	Menu getMenu(Site site, Long id) {
		QueryParameters qp = QueryParameters.with("site", site).add("id", id)
		return crudService.findSingle(Menu.class, qp)
	}

	@Override
	MenuContext setupMenuItem(MenuItem menuItem) {

		MenuItemType typeExtension = getMenuItemType(menuItem)
		MenuContext context = new MenuContext(menuItem, menuItem.menu)
		if (typeExtension != null) {
			typeExtension.setupMenuItem(context)
		}
		return context

	}

	@Override
	MenuItemType getMenuItemType(MenuItem menuItem) {
		String type = menuItem.type
		if (type != null && !type.empty) {
			for (MenuItemType typeExtension : Containers.get().findObjects(MenuItemType.class)) {
				if (type.equals(typeExtension.id)) {
					return typeExtension
				}
			}
		}
		return null
	}
	
	@Override
	List<MenuItem> getItems(Menu menu){
		return crudService.find(MenuItem.class, QueryParameters.with("menu", menu)
				.add("parentItem", QueryConditions.isNull())
				.orderBy("order"))
	}

}
