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
package tools.dynamia.cms.menus.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.menus.MenuContext
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.cms.menus.services.MenuService

@CMSModule
class MenuModule extends AbstractModule {

	private static final String PARAM_MENU = "menu"
    @Autowired
	private MenuService service

    MenuModule() {
		super("menu_module", "Menus and MenuItems", "menus/modules/menu")
        description = "Basic module for creation menubars width its items"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("Created at", "03-09-2014")
        setVariablesNames(PARAM_MENU, "menuitems")
    }

	@Override
    void init(ModuleContext context) {
		ModuleInstance mod = context.moduleInstance

        String menuId = mod.getParameterValue(PARAM_MENU)
        if (menuId == null) {
			menuId = "mainmenu"
        }

		Menu menu = null
        try {
			menu = service.getMenu(context.site, new Long(menuId))
        } catch (NumberFormatException e) {
			menu = service.getMenu(context.site, menuId)
        }

		if (menu != null) {
			List<MenuItem> itemsToDisplay = new ArrayList<>()
            for (MenuItem menuItem : service.getItems(menu)) {
				menuItem.subitems.size()
                MenuContext menuContext = service.setupMenuItem(menuItem)
                itemsToDisplay.add(menuContext.menuItem)
            }

			mod.addObject(PARAM_MENU, menu)
            mod.addObject("menuitems", itemsToDisplay)
        }

	}

}
