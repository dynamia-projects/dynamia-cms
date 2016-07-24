package tools.dynamia.cms.admin.users;

import tools.dynamia.cms.site.core.api.AdminModuleOption;
import tools.dynamia.cms.site.core.api.CMSExtension;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.navigation.NavigationElement;
import tools.dynamia.navigation.NavigationRestriction;

@CMSExtension
public class UserNavigationRestriction implements NavigationRestriction {

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public Boolean allowAccess(NavigationElement element) {
		AdminModuleOption option = (AdminModuleOption) element.getAttribute("OPTION");
		if (option != null) {
			User user = UserHolder.get().getCurrent();
			return (option.isEditorAllowed() && (user.getProfile() == UserProfile.EDITOR || user.getProfile() == UserProfile.ADMIN))
					|| (option.isAdminAllowed() && user.getProfile() == UserProfile.ADMIN);
		} else {
			return null;
		}
	}

}
