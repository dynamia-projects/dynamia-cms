package com.dynamia.cms.admin.core.zk;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.api.CMSExtension;

import tools.dynamia.domain.query.Parameters;
import tools.dynamia.navigation.NavigationElement;
import tools.dynamia.navigation.NavigationRestriction;

@CMSExtension
public class SystemNavigationRestriction implements NavigationRestriction {

	@Autowired
	private Parameters appParams;

	@Override
	public Boolean allowAccess(NavigationElement element) {
		if (element.getId().equals("system")) {
			String superAdminSite = appParams.getValue(DynamiaCMS.CFG_SUPER_ADMIN_SITE, "main");
			return superAdminSite.equals(SiteContext.get().getCurrent().getKey());
		}

		return null;
	}

	@Override
	public int getOrder() {

		return 0;
	}

}
