package tools.dynamia.cms.admin.shopping.actions

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.zul.Messagebox
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import toosl.dynamia.cms.site.shoppingcart.api.ShoppingOrderSenderException

@InstallAction
class SendShoppingOrderAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private CrudService crudService

    SendShoppingOrderAction() {
        name = "Send Order"
        description = "Send selected order to external system"
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
		ShoppingOrder order = (ShoppingOrder) evt.data
        if (order != null) {

			if (order.sended) {
				throw new ValidationError("Order " + order.number + " already was sended")
            }

			if (order.transaction.status != PaymentTransactionStatus.COMPLETED) {
				throw new ValidationError("Order is NOT completed")
            }

			UIMessages.showQuestion("Are you sure send order " + order.number + "?",  {
				try {
					service.sendOrder(order)
                    UIMessages.showMessage("Order " + order.number + " sended successfully")
                    evt.controller.doQuery()
                } catch (ShoppingOrderSenderException e) {
					Messagebox.show(e.message)
                } catch (ValidationError e) {
					UIMessages.showMessage(e.message, MessageType.WARNING)

                } catch (Exception e) {
					UIMessages.showMessage("Error sending order: " + e.message, MessageType.ERROR)
                    e.printStackTrace()
                }
			})
        }

	}

}
