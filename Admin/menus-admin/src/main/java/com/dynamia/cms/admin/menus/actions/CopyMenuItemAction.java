package com.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.menus.ui.MenuItemsUI;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.ActionGroup;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.MessageType;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.ZKUtil;

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
			ui.addAction(new SaveMenuItemAction(crudService));
			ZKUtil.showDialog(menuItem.getTitle(), ui, "90%", "90%");
		} else {
			UIMessages.showMessage("Select menu item", MessageType.ERROR);
		}

	}
}
