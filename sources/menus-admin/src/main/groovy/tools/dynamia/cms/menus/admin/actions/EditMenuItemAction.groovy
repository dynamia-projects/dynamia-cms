package tools.dynamia.cms.menus.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.menus.admin.controllers.MenuItemTreeCrudController
import tools.dynamia.cms.menus.admin.ui.MenuItemsUI
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.crud.actions.EditAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.crud.CrudView
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class EditMenuItemAction extends EditAction {

	@Autowired
	private CrudService crudService

    @Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(MenuItem.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		CrudView<MenuItem> crudView = (CrudView<MenuItem>) evt.view
        MenuItemTreeCrudController controller = (MenuItemTreeCrudController) evt.controller
        Menu menu = controller.menu

        MenuItem menuItem = (MenuItem) evt.data
        if (menuItem != null) {
			menuItem = crudService.reload(menuItem)
            MenuItemsUI ui = new MenuItemsUI(menuItem)
            ui.addAction(new SaveMenuItemAction(crudService, evt))
            ZKUtil.showDialog("Edit Item " + menuItem.name + " - " + menu.name, ui, "90%", "90%").maximizable = true
        } else {
			UIMessages.showMessage("Select menu item to edit", MessageType.ERROR)
        }

	}
}
