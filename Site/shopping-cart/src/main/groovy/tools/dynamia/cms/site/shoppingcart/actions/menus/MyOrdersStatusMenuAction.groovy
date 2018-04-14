package tools.dynamia.cms.site.shoppingcart.actions.menus

import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.site.users.domain.User

@CMSAction
public class MyOrdersStatusMenuAction implements UserMenuActionEnableable {

	@Override
	public String getLabel() {
		return "Estado de cuenta";
	}

	@Override
	public String getDescription() {
		return "Muestra el estado actual de la cartera";
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrder() {
		return 5;
	}

	@Override
	public String action() {
		return "shoppingcart/orders/status";
	}

	@Override
	public boolean isEnabled(User currentUser) {
		return currentUser.getSite().isCorporateSite();
	}

}
