package tools.dynamia.cms.admin.menus.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.menus.controllers.MenuItemTreeCrudController
import tools.dynamia.cms.admin.menus.ui.MenuItemsUI
import tools.dynamia.cms.site.menus.domain.Menu
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.crud.actions.NewAction
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.crud.CrudView
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class NewMenuItemAction extends NewAction {

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
        MenuItem selectedItem = (MenuItem) evt.data
        Menu menu = controller.menu

        controller.doCreate()

        MenuItemsUI ui = new MenuItemsUI(controller.entity)
        ui.addAction(new SaveMenuItemAction(crudService, evt))

        String title = "New Item for " + menu.name
        if (selectedItem != null) {
			title = "New Subitem for " + menu.name + " - " + selectedItem.name

        }

        ZKUtil.showDialog(title, ui, "90%", "90%").maximizable = true

    }
}
