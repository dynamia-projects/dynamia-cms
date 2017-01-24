package tools.dynamia.cms.site.users.actions;

import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.cms.site.users.domain.User;

@CMSAction
public class MyCustomersUserMenuAction implements UserMenuActionEnableable {

	@Override
	public String getLabel() {
		return "Mis Clientes";
	}

	@Override
	public String getDescription() {
		return "Muestra listado de clientes";
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public String action() {
		return "users/mycustomers";
	}

	@Override
	public boolean isEnabled(User currentUser) {
		return  currentUser.getSite().isCorporateSite() && currentUser.getProfile() == UserProfile.SELLER;
	}

}
