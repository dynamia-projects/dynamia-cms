package tools.dynamia.cms.site.shoppingcart.actions;

import org.springframework.stereotype.Component;

import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.users.actions.UserMenuAction;

@CMSAction
public class MyShoppingOrdersMenuAction implements UserMenuAction {

	@Override
	public String getLabel() {
		return "Mis Pedidos";
	}

	@Override
	public String getDescription() {
		return "Historial de Pedidos Completados";
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public int getOrder() {
		return 4;
	}

	@Override
	public String action() {
		return "shoppingcart/myorders";
	}

}
