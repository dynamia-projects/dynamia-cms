package com.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.ui.UIMessages;
import com.dynamia.tools.web.util.Callback;

@InstallAction
public class NotifyOrderCompletedAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private CrudService crudService;

	public NotifyOrderCompletedAction() {
		setName("Notify Order");
		setMenuSupported(true);

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
		final ShoppingOrder shoppingOrder = (ShoppingOrder) evt.getData();
		if (shoppingOrder != null) {

			UIMessages.showQuestion("Are you sure?", new Callback() {

				@Override
				public void doSomething() {
					service.notifyOrderCompleted(crudService.reload(shoppingOrder));
					UIMessages.showMessage("Notification sended ");

				}
			});
		}
	}

}
