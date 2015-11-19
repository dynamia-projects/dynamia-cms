package com.dynamia.cms.admin.core.zk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.domain.services.CrudService;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.crud.CrudView;

@Component("siteMgr")
@Scope("session")
public class SiteManager {

	@Autowired
	private SiteService service;
	
	@Autowired
	private CrudService crudService;

	public void clearCache() {
		Site site = SiteContext.get().getCurrent();
		if (site != null) {
			service.clearCache(site);
			UIMessages.showMessage("Site Cache cleared successfull");
		}

	}
	
	public void edit(){
		Site site = crudService.reload(SiteContext.get().getCurrent());
		
		
		CrudView.showUpdateView("Edit "+site, Site.class, site);
	}

}
