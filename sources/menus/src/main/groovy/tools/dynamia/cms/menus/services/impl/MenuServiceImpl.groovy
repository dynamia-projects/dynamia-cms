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
