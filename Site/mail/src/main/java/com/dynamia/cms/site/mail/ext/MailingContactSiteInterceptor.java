package com.dynamia.cms.site.mail.ext;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.mail.domain.MailingContact;

@CMSExtension
public class MailingContactSiteInterceptor extends SiteRequestInterceptorAdapter {

	
	@Override
	protected void afterRequest(Site site, ModelAndView mv) {
		if (mv != null && mv.getModel().get("mailingContact") == null) {
			mv.addObject("mailingContact", new MailingContact());
		}
	}
}
