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
package tools.dynamia.cms.site.menus.modules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.api.AbstractModule;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.cms.site.menus.MenuContext;
import tools.dynamia.cms.site.menus.domain.Menu;
import tools.dynamia.cms.site.menus.domain.MenuItem;
import tools.dynamia.cms.site.menus.services.MenuService;

@CMSModule
public class MenuModule extends AbstractModule {

	private static final String PARAM_MENU = "menu";
	@Autowired
	private MenuService service;

	public MenuModule() {
		super("menu_module", "Menus and MenuItems", "menus/modules/menu");
		setDescription("Basic module for creation menubars width its items");
		putMetadata("author", "Mario Serrano Leones");
		putMetadata("Created at", "03-09-2014");
	}

	@Override
	public void init(ModuleContext context) {
		ModuleInstance mod = context.getModuleInstance();

		String menuId = mod.getParameterValue(PARAM_MENU);
		if (menuId == null) {
			menuId = "mainmenu";
		}

		Menu menu = null;
		try {
			menu = service.getMenu(context.getSite(), new Long(menuId));
		} catch (NumberFormatException e) {
			menu = service.getMenu(context.getSite(), menuId);
		}

		if (menu != null) {
			List<MenuItem> itemsToDisplay = new ArrayList<>();
			for (MenuItem menuItem : service.getItems(menu)) {
				menuItem.getSubitems().size();
				MenuContext menuContext = service.setupMenuItem(menuItem);
				itemsToDisplay.add(menuContext.getMenuItem());
			}

			mod.addObject(PARAM_MENU, menu);
			mod.addObject("menuitems", itemsToDisplay);
		}

	}

}
