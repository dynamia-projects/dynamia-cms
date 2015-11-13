package com.dynamia.cms.admin.menus.actions;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.ActionEvent;

public class EditSubmenuAction extends NewSubmenuAction {

	public EditSubmenuAction(MenuItem parent) {
		super(parent);
		setName("Edit Submenu");
		setImage("edit");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		MenuItem submenu = (MenuItem) evt.getData();
		if (submenu != null) {
			showFormView("Edit submenu " + submenu.getName(), submenu, evt);
		}
	}

	@Override
	protected void save(MenuItemsUI ui, ActionEvent e) {

	}

}
