package com.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.admin.shopping.ui.ShoppingOrderDetailsUI;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.util.ZKUtil;

@InstallAction
public class ViewOrderDetailsAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService;

	public ViewOrderDetailsAction() {
		setName("View Details");
		setMenuSupported(true);
		setImage("info");
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
			shoppingOrder = crudService.reload(shoppingOrder);

			ShoppingOrderDetailsUI ui = new ShoppingOrderDetailsUI(shoppingOrder);
			ui.setVflex("1");
			ui.setStyle("overflow: auto");
			ZKUtil.showDialog("Shopping Order No. " + shoppingOrder.getNumber(), ui, "90%", "90%");
		}
	}

}
