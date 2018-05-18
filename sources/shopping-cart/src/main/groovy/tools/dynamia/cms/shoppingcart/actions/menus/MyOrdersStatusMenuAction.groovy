package tools.dynamia.cms.shoppingcart.actions.menus

import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.users.domain.User

@CMSAction
class MyOrdersStatusMenuAction implements UserMenuActionEnableable {

	@Override
    String getLabel() {
		return "Estado de cuenta"
    }

	@Override
    String getDescription() {
		return "Muestra el estado actual de la cartera"
    }

	@Override
    String getIcon() {
		// TODO Auto-generated method stub
		return null
    }

	@Override
    int getOrder() {
		return 5
    }

	@Override
    String action() {
		return "shoppingcart/orders/status"
    }

	@Override
    boolean isEnabled(User currentUser) {
		return currentUser.site.corporateSite
    }

}
