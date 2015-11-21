package com.dynamia.cms.admin.core.zk;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.api.CMSExtension;

import tools.dynamia.navigation.NavigationElement;
import tools.dynamia.navigation.NavigationRestriction;

@CMSExtension
public class SystemNavigationRestriction implements NavigationRestriction {


	@Override
	public Boolean allowAccess(NavigationElement element) {
		if (element.getId().equals("system")) {
			return SiteContext.get().isSuperAdmin();
		}

		return null;
	}

	@Override
	public int getOrder() {

		return 0;
	}

}
