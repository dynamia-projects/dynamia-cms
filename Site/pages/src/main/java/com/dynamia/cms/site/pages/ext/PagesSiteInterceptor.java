package com.dynamia.cms.site.pages.ext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptor;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;

@CMSExtension
public class PagesSiteInterceptor implements SiteRequestInterceptor {

	@Override
	public void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		Page page = (Page) modelAndView.getModel().get("page");
		if (page == null) {
			page = new Page();
			page.setSite(site);
			page.setAlias(SiteContext.get().getCurrentURI());
			page.setTitle((String) modelAndView.getModel().get("title"));
			page.setSubtitle((String) modelAndView.getModel().get("subtitle"));
			page.setIcon((String) modelAndView.getModel().get("icon"));
			page.setType("auto");
			
			modelAndView.addObject("page", page);
		}

	}

}
