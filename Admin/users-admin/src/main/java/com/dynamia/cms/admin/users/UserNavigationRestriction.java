package com.dynamia.cms.admin.users;

import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.domain.enums.UserProfile;

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
