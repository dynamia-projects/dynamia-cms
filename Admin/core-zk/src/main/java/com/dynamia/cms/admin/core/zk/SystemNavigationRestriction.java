package com.dynamia.cms.admin.core.zk;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.tools.domain.query.Parameters;
import com.dynamia.tools.web.navigation.NavigationElement;
import com.dynamia.tools.web.navigation.NavigationRestriction;

@CMSExtension
public class SystemNavigationRestriction implements NavigationRestriction {

	private static final String CMS_CONFIG_SUPER_ADMIN_SITE = "CMSConfig_superAdminSite";
	@Autowired
	private Parameters appParams;

	@Override
	public Boolean allowAccess(NavigationElement element) {
		if (element.getId().equals("system")) {
			String superAdminSite = appParams.getValue(CMS_CONFIG_SUPER_ADMIN_SITE, "main");
			return superAdminSite.equals(SiteContext.get().getCurrent().getKey());
		}

		return null;
	}

	@Override
	public int getOrder() {

		return 0;
	}

}
