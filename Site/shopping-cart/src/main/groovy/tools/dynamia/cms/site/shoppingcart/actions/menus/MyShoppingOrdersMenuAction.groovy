package tools.dynamia.cms.site.shoppingcart.actions.menus

import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.site.users.domain.User

@CMSAction
public class MyShoppingOrdersMenuAction implements UserMenuActionEnableable {

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
		return "shoppingcart/orders";
	}

	@Override
	public boolean isEnabled(User currentUser) {
		return currentUser.getSite().isCorporateSite();
	}
}
