/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.api.SiteRequestInterceptor;
import com.dynamia.cms.site.core.controllers.CacheController;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.integration.Containers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author mario
 */
public class SiteHandleInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SiteService service;

	@Autowired
	private CacheController cacheController;

	private final LoggingService logger = new SLF4JLoggingService(SiteHandleInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Site site = service.getSite(request);
		if (site == null) {
			site = service.getMainSite();
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

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		if (modelAndView == null) {
			return;
		}

		checkCache(request);

		Site site = service.getSite(request);
		if (site == null) {
			site = service.getMainSite();
		}

		loadSiteMetadata(site, modelAndView);

		try {
			for (SiteRequestInterceptor interceptor : Containers.get().findObjects(SiteRequestInterceptor.class)) {
				interceptor.afterRequest(site, request, response, modelAndView);
			}
		} catch (Exception e) {
			logger.error("Error calling Site interceptor", e);
		}

		if (site != null && site.isOffline()) {
			shutdown(site, modelAndView);
		}
	}

	private void checkCache(HttpServletRequest request) {
		if (request != null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Long lastCacheClear = (Long) session.getAttribute("lastCacheClear");
				if (lastCacheClear == null) {
					lastCacheClear = 0L;
				}
				if (cacheController.getLastCacheClear() > lastCacheClear) {
					lastCacheClear = cacheController.getLastCacheClear();
					session.setAttribute("lastCacheClear", lastCacheClear);
					session.removeAttribute("site");
				}
			}
		}
	}

	private void shutdown(Site site, ModelAndView mv) {
		mv.setViewName("error/offline");
		mv.addObject("title", "OFFLINE!");
		mv.addObject("site", site);
		mv.addObject("offlineIcon", site.getOfflineIcon());
		mv.addObject("offlineMessage", site.getOfflineMessage());

	}

	private void loadSiteMetadata(Site site, ModelAndView mv) {
		if (site != null && mv != null) {
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

}
