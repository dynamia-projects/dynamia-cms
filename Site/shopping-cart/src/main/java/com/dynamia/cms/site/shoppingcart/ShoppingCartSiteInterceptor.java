package com.dynamia.cms.site.shoppingcart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptor;
import com.dynamia.cms.site.core.domain.Site;

@CMSExtension
public class ShoppingCartSiteInterceptor implements SiteRequestInterceptor {

	@Override
	public void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response) {

	}

	@Override
	public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

		ShoppingCartUtils.loadConfig(site, modelAndView);

	}

}
