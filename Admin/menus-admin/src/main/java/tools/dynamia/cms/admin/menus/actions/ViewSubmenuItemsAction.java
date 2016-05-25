package tools.dynamia.cms.admin.menus.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.admin.core.zk.actions.DecreaseOrderAction;
import tools.dynamia.cms.admin.core.zk.actions.IncreaseOrderAction;
import tools.dynamia.cms.admin.menus.ui.SubmenuItemsUI;
import tools.dynamia.cms.site.menus.domain.MenuItem;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

@InstallAction
public class ViewSubmenuItemsAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService;

	@Autowired
	private IncreaseOrderAction increaseOrderAction;

	@Autowired
	private DecreaseOrderAction decreaseOrderAction;

	public ViewSubmenuItemsAction() {
		setName("Submenus");
		setImage("menu");
		setMenuSupported(true);
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
			SubmenuItemsUI ui = new SubmenuItemsUI(menuItem, evt);
			ui.addAction(new NewSubmenuAction(menuItem));
			ui.addAction(new EditSubmenuAction(menuItem));
			ui.addAction(new DeleteSubmenuAction(menuItem));
			ui.addAction(increaseOrderAction);
			ui.addAction(decreaseOrderAction);

			ZKNavigationManager.getInstance()
					.setCurrentPage(new ComponentPage("viewSubmenus" + menuItem.getId(), "Submenus - " + menuItem.getName(), ui));
		} else {
			UIMessages.showMessage("Select menu item to view submenus", MessageType.ERROR);
		}

	}
}