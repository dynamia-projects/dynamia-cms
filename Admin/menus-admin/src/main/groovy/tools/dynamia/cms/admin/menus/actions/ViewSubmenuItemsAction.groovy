package tools.dynamia.cms.admin.menus.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.menus.controllers.MenuItemTreeCrudController
import tools.dynamia.cms.site.menus.domain.Menu
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.crud.CrudView
import tools.dynamia.zk.navigation.ComponentPage
import tools.dynamia.zk.navigation.ZKNavigationManager

@InstallAction
class ViewSubmenuItemsAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService

    ViewSubmenuItemsAction() {
		setName("Items")
        setImage("menu")
        setMenuSupported(true)
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Menu.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Menu menu = (Menu) evt.getData()
        if (menu != null) {
			menu = crudService.reload(menu)
            CrudView<MenuItem> ui = (CrudView<MenuItem>) Viewers.getView(MenuItem.class, "crud", null)
            MenuItemTreeCrudController controller = (MenuItemTreeCrudController) ui.getController()
            controller.setMenu(menu)
            controller.doQuery()
            ZKNavigationManager.getInstance().setCurrentPage(
					new ComponentPage("viewSubmenus" + menu.getId(),  menu.getName()+" Items", ui))
        } else {
			UIMessages.showMessage("Select menu to view submenus", MessageType.ERROR)
        }

	}
}
