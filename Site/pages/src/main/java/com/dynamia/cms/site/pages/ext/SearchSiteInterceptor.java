/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.ext;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.SearchForm;

/**
 *
 * @author mario
 */
@CMSExtension
public class SearchSiteInterceptor extends SiteRequestInterceptorAdapter {

	@Override
	protected void afterRequest(Site site, ModelAndView mv) {
		if (mv != null && mv.getModel().get("searchForm") == null) {
			mv.addObject("searchForm", new SearchForm());
		}
	}

}
