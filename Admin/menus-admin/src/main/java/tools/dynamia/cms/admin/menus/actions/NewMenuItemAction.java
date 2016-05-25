package tools.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.admin.menus.ui.MenuItemsUI;
import tools.dynamia.cms.site.menus.domain.Menu;
import tools.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.NewAction;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.zk.crud.SubcrudController;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

@InstallAction
public class NewMenuItemAction extends NewAction {

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

		SubcrudController<MenuItem> controller = (SubcrudController<MenuItem>) evt.getController();

		controller.newEntity();
		MenuItem menuItem = (MenuItem) evt.getController().getEntity();
		menuItem.setMenu((Menu) controller.getParentEntity());

		MenuItemsUI ui = new MenuItemsUI(menuItem);
		ui.addAction(new SaveMenuItemAction(crudService, evt));
		ZKNavigationManager.getInstance().setCurrentPage(new ComponentPage("newMenu"+System.currentTimeMillis(), "New Menu ", ui));

	}
}
