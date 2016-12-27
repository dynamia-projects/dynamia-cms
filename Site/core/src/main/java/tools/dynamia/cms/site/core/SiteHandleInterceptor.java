/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import tools.dynamia.cms.site.core.api.SiteRequestInterceptor;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
public class SiteHandleInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SiteService service;

	private final LoggingService logger = new SLF4JLoggingService(SiteHandleInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Site site = getCurrentSite(request);
		if (site != null && site.isOffline()) {

			if (site.getOfflineRedirect() != null && !site.getOfflineRedirect().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				response.setHeader("Location", site.getOfflineRedirect());
				return false;
			}
			return true;
		}

		try {
			for (SiteRequestInterceptor interceptor : Containers.get().findObjects(SiteRequestInterceptor.class)) {
				interceptor.beforeRequest(site, request, response);
			}
		} catch (Exception e) {
			logger.error("Error calling Site interceptor", e);
			return false;
		}

		return true;
	}

	private Site getCurrentSite(HttpServletRequest request) {
		Site site = SiteContext.get().getCurrent();
		if (site == null) {
			site = service.getSite(request);
			SiteContext.get().setCurrent(site);
		}
		if (site == null) {
			site = service.getMainSite();
			SiteContext.get().setCurrent(site);
		}
		SiteContext.get().setCurrentURI(request.getRequestURI());
		SiteContext.get().setCurrentURL(request.getRequestURL().toString());
		if (SiteContext.get().getSiteURL() == null) {
			String siteURL = "http://" + request.getServerName();
			if (request.getServerPort() != 80) {
				siteURL = siteURL + ":" + request.getServerPort();
			}
			SiteContext.get().setSiteURL(siteURL);
		}
		return site;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (modelAndView == null) {
			return;
		}

	
		Site site = getCurrentSite(request);
		
		boolean isJson = checkJsonRequest(request, modelAndView);		
		if (isJson) {
			return;
		}
		loadSiteMetadata(site, modelAndView);

		if (!site.isOffline()) {			
			
			try {
				for (SiteRequestInterceptor interceptor : Containers.get().findObjects(SiteRequestInterceptor.class)) {
					interceptor.afterRequest(site, request, response, modelAndView);
				}
			} catch (Exception e) {
				logger.error("Error calling Site interceptor", e);
			}
		}else{
			shutdown(site, modelAndView);
		}

	}

	private boolean checkJsonRequest(HttpServletRequest request, ModelAndView modelAndView) {
		boolean json = CMSUtil.isJson(request);

		return json;
	}

	private void loadSiteMetadata(Site site, ModelAndView mv) {
		if (site != null && mv != null) {
			mv.addObject("siteKey", site.getKey());
			mv.addObject("site", site);
			mv.addObject("metaAuthor", site.getMetadataAuthor());
			mv.addObject("metaRights", site.getMetadataRights());
			if (!mv.getModel().containsKey("metaDescription")) {
				mv.addObject("metaDescription", site.getMetadataDescription());
			}
			if (!mv.getModel().containsKey("metaKeywords")) {
				mv.addObject("metaKeywords", site.getMetadataKeywords());
			}

		}
	}

	public static void shutdown(Site site, ModelAndView mv) {
		mv.setViewName("error/offline");
		mv.addObject("title", "OFFLINE!");
		mv.addObject("site", site);
		mv.addObject("siteKey", site.getKey());
		mv.addObject("offlineIcon", site.getOfflineIcon());
		mv.addObject("offlineMessage", site.getOfflineMessage());

	}

}
