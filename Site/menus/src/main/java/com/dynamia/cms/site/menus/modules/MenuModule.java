package com.dynamia.cms.site.menus.modules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.menus.domain.Menu;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.services.MenuService;

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
			List<MenuItem> items = new ArrayList<>(menu.getItems());
			for (MenuItem menuItem : items) {
				menuItem.getSubitems().size();
				service.setupMenuItem(menuItem);
			}

			mod.addObject(PARAM_MENU, menu);
			mod.addObject("menuitems", items);
		}

	}

}
