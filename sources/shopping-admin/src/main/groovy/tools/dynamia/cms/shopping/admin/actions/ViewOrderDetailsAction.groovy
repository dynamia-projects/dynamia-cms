package tools.dynamia.cms.shopping.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shopping.admin.ui.ShoppingOrderDetailsUI
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.crud.actions.ViewDataAction
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ViewOrderDetailsAction extends ViewDataAction {

	@Autowired
	private CrudService crudService

    ViewOrderDetailsAction() {
        name = "Details"
        menuSupported = true
        image = "table"
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder shoppingOrder = (ShoppingOrder) evt.data
        if (shoppingOrder != null) {
			shoppingOrder = crudService.reload(shoppingOrder)

            ShoppingOrderDetailsUI ui = new ShoppingOrderDetailsUI(shoppingOrder)
            ui.vflex = "1"
            ui.style = "overflow: auto"
            ZKUtil.showDialog("Shopping Order No. " + shoppingOrder.number, ui, "90%", "90%")
        }
	}
	
	@Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

}
