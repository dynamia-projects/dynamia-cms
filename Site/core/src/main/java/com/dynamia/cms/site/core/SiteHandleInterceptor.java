/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.api.SiteRequestInterceptor;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.integration.Containers;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Site site = service.getSite(request);
        if (site == null) {
            site = service.getMainSite();
        }

        if (site.isOffline()) {
            modelAndView.clear();
            modelAndView.addObject("site", site);
            modelAndView.setViewName("site/offline");
        } else {
            try {
                for (SiteRequestInterceptor interceptor : Containers.get().findObjects(SiteRequestInterceptor.class)) {
                    interceptor.afterRequest(site, request, response, modelAndView);
                }
            } catch (Exception e) {
                logger.error("Error calling Site interceptor", e);
            }
        }

    }

}
