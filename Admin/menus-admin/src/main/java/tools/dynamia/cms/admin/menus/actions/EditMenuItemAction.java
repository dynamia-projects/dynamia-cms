package tools.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.admin.menus.ui.MenuItemsUI;
import tools.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.EditAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

@InstallAction
public class EditMenuItemAction extends EditAction {

	@Autowired
	private CrudService crudService;

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
			MenuItemsUI ui = new MenuItemsUI(menuItem);
			ui.addAction(new SaveMenuItemAction(crudService,evt));
			ZKNavigationManager.getInstance().setCurrentPage(new ComponentPage("editMenu"+menuItem.getId(), "Edit Menu "+menuItem, ui));
		} else {
			UIMessages.showMessage("Select menu item to edit", MessageType.ERROR);
		}

	}
}
