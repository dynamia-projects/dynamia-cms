package tools.dynamia.cms.admin.shopping.actions;

import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.crud.actions.ViewDataAction;

@InstallAction
public class OrderPaymentDetailAction extends ViewDataAction {

	public OrderPaymentDetailAction() {
		setName("Payment");
		setMenuSupported(true);

	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder order = (ShoppingOrder) evt.getData();
		if (order != null) {
			super.actionPerformed(
					new CrudActionEvent(order.getTransaction(), evt.getSource(), evt.getView(), evt.getController()));
		} else {
			UIMessages.showMessage("Select order", MessageType.WARNING);
		}
	}
}
