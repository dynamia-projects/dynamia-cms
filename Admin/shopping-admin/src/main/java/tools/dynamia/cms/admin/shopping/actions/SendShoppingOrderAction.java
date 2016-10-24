package tools.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class SendShoppingOrderAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service;

	public SendShoppingOrderAction() {
		setName("Send Order");
		setDescription("Send selected order to external system");
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder order = (ShoppingOrder) evt.getData();
		if (order != null) {
			if (order.isSended()) {
				throw new ValidationError("Order " + order.getNumber() + " already was sended");
			}

			if (order.getTransaction().getStatus() != PaymentTransactionStatus.COMPLETED) {
				throw new ValidationError("Order is NOT completed");
			}

			UIMessages.showQuestion("Are you sure ?", () -> {
				try {
					service.sendOrder(order);
					UIMessages.showMessage("Order " + order.getNumber() + " sended successfully");
					evt.getController().doQuery();
				} catch (ValidationError e) {
					UIMessages.showMessage(e.getMessage(), MessageType.WARNING);

				} catch (Exception e) {
					UIMessages.showMessage("Error sending order: " + e.getMessage(), MessageType.ERROR);
					e.printStackTrace();
				}
			});
		}

	}

}
