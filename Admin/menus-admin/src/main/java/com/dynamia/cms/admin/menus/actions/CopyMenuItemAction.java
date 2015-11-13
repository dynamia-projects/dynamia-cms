package com.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class CopyMenuItemAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService;

	public CopyMenuItemAction() {
		setGroup(ActionGroup.get("CRUD"));
		setName("Copy");
		setImage("copy");
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(MenuItem.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		MenuItem menuItem = (MenuItem) evt.getData();
		if (menuItem != null) {
			menuItem = crudService.reload(menuItem);
			menuItem = menuItem.clone();
			MenuItemsUI ui = new MenuItemsUI(menuItem);
			ui.addAction(new SaveMenuItemAction(crudService, evt));
			ZKUtil.showDialog(menuItem.getTitle(), ui, "90%", "90%");
		} else {
			UIMessages.showMessage("Select menu item", MessageType.ERROR);
		}

	}
}
