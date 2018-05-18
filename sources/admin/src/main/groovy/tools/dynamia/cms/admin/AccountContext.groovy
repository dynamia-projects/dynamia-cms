package tools.dynamia.cms.admin

import org.springframework.stereotype.Component
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site

@Component("accountContext")
class AccountContext {
	
	Site getAccount(){
		return SiteContext.get().current
    }

}
