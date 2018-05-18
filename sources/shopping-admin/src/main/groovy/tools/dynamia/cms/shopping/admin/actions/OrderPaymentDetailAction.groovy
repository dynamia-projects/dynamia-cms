package tools.dynamia.cms.shopping.admin.actions

import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.crud.actions.ViewDataAction

@InstallAction
class OrderPaymentDetailAction extends ViewDataAction {

	OrderPaymentDetailAction() {
        name = "Payment"
        menuSupported = true

    }

	@Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder order = (ShoppingOrder) evt.data
        if (order != null) {
			super.actionPerformed(
					new CrudActionEvent(order.transaction, evt.source, evt.view, evt.controller))
        } else {
			UIMessages.showMessage("Select order", MessageType.WARNING)
        }
	}
}
