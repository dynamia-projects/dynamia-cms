package tools.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Window;

import tools.dynamia.cms.admin.shopping.ui.ShoppingOrderShipmentUI;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.commons.Messages;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class ShipOrderAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private CrudService crudService;

	public ShipOrderAction() {
		setName("Ship Order");
		setMenuSupported(true);
		setImage("truck");
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
		ShoppingOrder shoppingOrder = (ShoppingOrder) evt.getData();
		if (shoppingOrder != null) {
			try {
				shoppingOrder = crudService.reload(shoppingOrder);
				basicValidation(shoppingOrder);
				ShoppingOrderShipmentUI ui = new ShoppingOrderShipmentUI(shoppingOrder);
				ui.addAction(new ConfirmShipOrderAction());
				ZKUtil.showDialog("Ship Shopping Order No. " + shoppingOrder.getNumber(), ui, "90%", "90%");
			} catch (ValidationError e) {
				UIMessages.showMessage(e.getMessage(), MessageType.ERROR);
			}

		}
	}

	private void basicValidation(ShoppingOrder shoppingOrder) {

		if (!shoppingOrder.isCompleted() || !shoppingOrder.getTransaction().isConfirmed()) {
			throw new ValidationError(Messages.get(ShoppingCartService.class, "OrderNotConfirmed", shoppingOrder.getNumber()));
		}

		if (shoppingOrder.isShipped()) {
			throw new ValidationError(Messages.get(ShoppingCartService.class, "OrderAlreadyShipped", shoppingOrder.getNumber()));
		}

	}

	class ConfirmShipOrderAction extends AbstractAction {

		public ConfirmShipOrderAction() {
			setName("Confirm Shopping Order");
		}

		@Override
		public void actionPerformed(ActionEvent evt) {
			try {
				ShoppingOrder shoppingOrder = (ShoppingOrder) evt.getData();
				service.shipOrder(shoppingOrder);
				ShoppingOrderShipmentUI ui = (ShoppingOrderShipmentUI) evt.getSource();
				if (ui.getParent() instanceof Window) {
					ui.getParent().detach();
				}
				UIMessages.showMessage(Messages.get(ShoppingCartService.class, "OrderShipedSuccesfull", shoppingOrder.getNumber()));
			} catch (ValidationError e) {
				UIMessages.showMessage(e.getMessage(), MessageType.ERROR);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
