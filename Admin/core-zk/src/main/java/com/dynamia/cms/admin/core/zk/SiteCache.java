package com.dynamia.cms.admin.core.zk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.ui.UIMessages;

@Component("siteCache")
@Scope("session")
public class SiteCache {

	@Autowired
	private SiteService service;

	public void clear() {
		Site site = SiteContext.get().getCurrent();
		if (site != null) {
			service.clearCache(site);
			UIMessages.showMessage("Site Cache cleared successfull");
		}

	}

}
