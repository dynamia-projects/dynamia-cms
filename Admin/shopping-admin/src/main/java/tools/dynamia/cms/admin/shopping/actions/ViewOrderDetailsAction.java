package tools.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.admin.shopping.ui.ShoppingOrderDetailsUI;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.zk.util.ZKUtil;

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
