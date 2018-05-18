package tools.dynamia.cms.users.actions

import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User

@CMSAction
class MyCustomersUserMenuAction implements UserMenuActionEnableable {

	@Override
    String getLabel() {
		return "Mis Clientes"
    }

	@Override
    String getDescription() {
		return "Muestra listado de clientes"
    }

	@Override
    String getIcon() {
		return null
    }

	@Override
    int getOrder() {
		return -1
    }

	@Override
    String action() {
		return "users/mycustomers"
    }

	@Override
    boolean isEnabled(User currentUser) {
		return  currentUser.site.corporateSite && currentUser.profile == UserProfile.SELLER
    }

}
