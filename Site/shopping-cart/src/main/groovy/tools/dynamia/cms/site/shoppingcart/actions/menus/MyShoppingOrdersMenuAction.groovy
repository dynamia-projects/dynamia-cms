package tools.dynamia.cms.site.shoppingcart.actions.menus

import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.site.users.domain.User

@CMSAction
class MyShoppingOrdersMenuAction implements UserMenuActionEnableable {

	@Override
    String getLabel() {
		return "Mis Pedidos"
    }

	@Override
    String getDescription() {
		return "Historial de Pedidos Completados"
    }

	@Override
    String getIcon() {
		return null
    }

	@Override
    int getOrder() {
		return 4
    }

	@Override
    String action() {
		return "shoppingcart/orders"
    }

	@Override
    boolean isEnabled(User currentUser) {
		return currentUser.getSite().isCorporateSite()
    }
}
