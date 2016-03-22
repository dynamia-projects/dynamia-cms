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
package com.dynamia.cms.site.menus.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.menus.MenuContext;
import com.dynamia.cms.site.menus.api.MenuItemType;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.services.MenuService;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private CrudService crudService;

	@Override
	@Cacheable(value = "menus", key = "#site.key")
	public Menu getMainMenu(Site site) {
		QueryParameters qp = QueryParameters.with("site", site);
		return crudService.findSingle(Menu.class, qp);
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#alias")
	public Menu getMenu(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("site", site).add("alias", alias);
		return crudService.findSingle(Menu.class, qp);
	}

	@Override
	@Cacheable(value = "menus", key = "#site.key+#id")
	public Menu getMenu(Site site, Long id) {
		QueryParameters qp = QueryParameters.with("site", site).add("id", id);
		return crudService.findSingle(Menu.class, qp);
	}

	@Override
	public MenuContext setupMenuItem(MenuItem menuItem) {

		MenuItemType typeExtension = getMenuItemType(menuItem);
		MenuContext context = new MenuContext(menuItem, menuItem.getMenu());
		if (typeExtension != null) {
			typeExtension.setupMenuItem(context);
		}
		return context;

	}

	@Override
	public MenuItemType getMenuItemType(MenuItem menuItem) {
		String type = menuItem.getType();
		if (type != null && !type.isEmpty()) {
			for (MenuItemType typeExtension : Containers.get().findObjects(MenuItemType.class)) {
				if (type.equals(typeExtension.getId())) {
					return typeExtension;
				}
			}
		}
		return null;
	}

}
