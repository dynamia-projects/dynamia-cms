package tools.dynamia.cms.admin.shopping.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.shopping.ui.ShoppingOrderDetailsUI
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
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
		setName("Details")
        setMenuSupported(true)
        setImage("table")
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
		ShoppingOrder shoppingOrder = (ShoppingOrder) evt.getData()
        if (shoppingOrder != null) {
			shoppingOrder = crudService.reload(shoppingOrder)

            ShoppingOrderDetailsUI ui = new ShoppingOrderDetailsUI(shoppingOrder)
            ui.setVflex("1")
            ui.setStyle("overflow: auto")
            ZKUtil.showDialog("Shopping Order No. " + shoppingOrder.getNumber(), ui, "90%", "90%")
        }
	}
	
	@Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

}
