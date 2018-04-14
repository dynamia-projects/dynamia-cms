package tools.dynamia.cms.site.users.actions

import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.api.UserProfile
import tools.dynamia.cms.site.users.domain.User

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
		return  currentUser.getSite().isCorporateSite() && currentUser.getProfile() == UserProfile.SELLER
    }

}
