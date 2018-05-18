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
package tools.dynamia.cms.menus.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.menus.MenuContext
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
class MenuServiceImpl implements MenuService {

	@Autowired
	private CrudService crudService

	@Override
	@Cacheable(value = "menus", key = "#site.key")
	tools.dynamia.cms.menus.domain.Menu getMainMenu(Site site) {
		QueryParameters qp = QueryParameters.with("site", site)
		return crudService.findSingle(tools.dynamia.cms.menus.domain.Menu.class, qp)
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#alias")
	tools.dynamia.cms.menus.domain.Menu getMenu(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("site", site).add("alias", alias)
		return crudService.findSingle(tools.dynamia.cms.menus.domain.Menu.class, qp)
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#id")
	tools.dynamia.cms.menus.domain.Menu getMenu(Site site, Long id) {
		QueryParameters qp = QueryParameters.with("site", site).add("id", id)
		return crudService.findSingle(tools.dynamia.cms.menus.domain.Menu.class, qp)
	}

	@Override
	MenuContext setupMenuItem(tools.dynamia.cms.menus.domain.MenuItem menuItem) {

		tools.dynamia.cms.menus.api.MenuItemType typeExtension = getMenuItemType(menuItem)
		MenuContext context = new MenuContext(menuItem, menuItem.menu)
		if (typeExtension != null) {
			typeExtension.setupMenuItem(context)
		}
		return context

	}

	@Override
	tools.dynamia.cms.menus.api.MenuItemType getMenuItemType(tools.dynamia.cms.menus.domain.MenuItem menuItem) {
		String type = menuItem.type
		if (type != null && !type.empty) {
			for (tools.dynamia.cms.menus.api.MenuItemType typeExtension : Containers.get().findObjects(tools.dynamia.cms.menus.api.MenuItemType.class)) {
				if (type.equals(typeExtension.id)) {
					return typeExtension
				}
			}
		}
		return null
	}
	
	@Override
	List<tools.dynamia.cms.menus.domain.MenuItem> getItems(tools.dynamia.cms.menus.domain.Menu menu){
		return crudService.find(tools.dynamia.cms.menus.domain.MenuItem.class, QueryParameters.with("menu", menu)
				.add("parentItem", QueryConditions.isNull())
				.orderBy("order"))
	}

}
