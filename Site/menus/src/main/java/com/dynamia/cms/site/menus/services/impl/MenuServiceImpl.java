/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;

/**
 *
 * @author mario
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
	public void setupMenuItem(MenuItem menuItem) {

		MenuItemType typeExtension = getMenuItemType(menuItem);
		if (typeExtension != null) {
			MenuContext context = new MenuContext(menuItem, menuItem.getMenu());
			typeExtension.setupMenuItem(context);
		}

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
