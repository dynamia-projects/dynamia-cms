package tools.dynamia.cms.admin.menus.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.menus.domain.MenuItem
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.actions.DeleteAction
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class DeleteMenuItemAction extends DeleteAction {

    @Autowired
    private CrudService crudService

    @Override
    void actionPerformed(CrudActionEvent evt) {
        MenuItem submenu = (MenuItem) evt.getData()
        if (submenu != null) {
            UIMessages.showQuestion("Are you sure?", {
                crudService.executeWithinTransaction {

                    try {
                        crudService.execute("DELETE FROM MenuItem mi where mi.parentItem.id = :id",
                                QueryParameters.with("id", submenu.getId()))

                        crudService.execute("DELETE FROM MenuItem mi where mi.id = :id",
                                QueryParameters.with("id", submenu.getId()))
                        UIMessages.showMessage("Menu item deleted succesfull")
                        evt.getController().doQuery()
                    } catch (Exception e) {
                        UIMessages
                                .showMessage("Error deleting item. Try deleting subitems first: " + e.getMessage())
                        e.printStackTrace()
                    }
                }
            })
        } else {
            UIMessages.showMessage("Select menu item to delete", MessageType.WARNING)
        }
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MenuItem.class)
    }

}
