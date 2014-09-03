/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.ext;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSModules;
import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteParameter;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.core.services.impl.ModulesService;

/**
 *
 * @author mario
 */
@CMSExtension
public class UtilsVarSiteInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private SiteService service;

	@Autowired
	private ModulesService modulesService;

	@Override
	public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		if (modelAndView == null) {
			return;
		}

		String requestURI = request.getRequestURI();
		String requestURL = request.getRequestURL().toString();

		if (requestURI != null) {
			modelAndView.addObject("currentURI", requestURI);
		}
		if (requestURL != null) {
			modelAndView.addObject("currentURL", requestURL);
		}
		modelAndView.addObject("site", site);
		modelAndView.addObject("siteParams", createParams(site));
		modelAndView.addObject("cmsUtil", new CMSUtil(site));
		modelAndView.addObject("cmsModules", new CMSModules(site, modulesService));
	}

	private Object createParams(Site site) {
		Map<String, String> map = new HashMap<>();
		try {
			for (SiteParameter p : service.getSiteParameters(site)) {
				map.put(p.getName(), p.getValue());
			}
		} catch (Exception e) {
		}
		return map;
	}

}
