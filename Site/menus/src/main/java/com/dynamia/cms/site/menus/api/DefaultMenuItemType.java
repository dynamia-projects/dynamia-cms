package com.dynamia.cms.site.menus.api;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.menus.MenuContext;

@CMSExtension
public class DefaultMenuItemType implements MenuItemType {

	@Override
	public String getId() {
		return "default";
	}

	@Override
	public String getName() {
		return "Default Menu Item";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void setupMenuItem(MenuContext context) {
		// TODO Auto-generated method stub

	}

}
