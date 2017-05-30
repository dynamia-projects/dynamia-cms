package tools.dynamia.cms.admin.core;

import org.springframework.stereotype.Component;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;

@Component("accountContext")
public class AccountContext {
	
	public Site getAccount(){
		return SiteContext.get().getCurrent();
	}

}