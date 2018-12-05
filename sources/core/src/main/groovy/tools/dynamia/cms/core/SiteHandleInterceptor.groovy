/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.core

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import tools.dynamia.cms.core.api.SiteRequestInterceptor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
class SiteHandleInterceptor extends HandlerInterceptorAdapter {

    private static final String SPRING_MVC_MODEL = "_springMvcModel"
    private static final List<String> EXCLUDES = ["jpg", "gif", "png", "tiff", "css", "js",
                                                  "svg", "bmp", "ttf", "jpeg", "json"]

    private final LoggingService logger = new SLF4JLoggingService(SiteHandleInterceptor.class)

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Site site = getCurrentSite(request)
        if (site != null && site.offline && site.offlineRedirect != null && !site.offlineRedirect.empty) {
            response.status = HttpServletResponse.SC_MOVED_PERMANENTLY
            response.setHeader("Location", site.offlineRedirect)
            return false

        }

        try {
            if (isInterceptable(request)) {
                for (SiteRequestInterceptor interceptor : Containers.get().findObjects(SiteRequestInterceptor.class)) {
                    interceptor.beforeRequest(site, request, response)
                }
            }
        } catch (Exception e) {
            logger.error("Error calling Site interceptor", e)
            return false
        }

        return true
    }

    private static boolean isUserLogin(HttpServletRequest request) {
        def usersURI = ["/users/login", "/users/logout", "/users/resetpassword", "/cms-admin"]
        return usersURI.contains(request.requestURI)
    }


    private static boolean isInterceptable(HttpServletRequest request) {
        String uri = request.requestURI
        for (String ext : EXCLUDES) {
            if (uri.endsWith("exception") || uri.endsWith("." + ext)) {
                return false
            }
        }
        return true
    }

    private static Site getCurrentSite(HttpServletRequest request) {
        SiteService service = Containers.get().findObject(SiteService.class)
        Site site = SiteContext.get().current
        if (site == null) {
            site = service.getSite(request)
            SiteContext.get().current = site
        }
        if (site == null) {
            site = service.mainSite
            SiteContext.get().current = site
        }
        SiteContext.get().currentURI = request.requestURI
        SiteContext.get().currentURL = request.requestURL.toString()
        if (SiteContext.get().siteURL == null) {
            String siteURL = "http://" + request.serverName
            if (request.serverPort != 80) {
                siteURL = siteURL + ":" + request.serverPort
            }
            SiteContext.get().siteURL = siteURL
        }
        SiteContext.get().reload()
        site = SiteContext.get().current
        return site
    }

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) {
            return
        }

        Site site = getCurrentSite(request)

        boolean isJson = CMSUtil.isJson(request)
        if (isJson) {
            return
        }
        loadSiteMetadata(site, modelAndView)



        try {
            if (isInterceptable(request)) {
                for (SiteRequestInterceptor interceptor : Containers.get()
                        .findObjects(SiteRequestInterceptor.class)) {
                    interceptor.afterRequest(site, request, response, modelAndView)
                }
            }
        } catch (Exception e) {
            logger.error("Error calling Site interceptor", e)
        }

        if (site.offline && !isUserLogin(request) && request.userPrincipal == null) {
            shutdown(site, modelAndView)
        }

        modelAndView.addObject(SPRING_MVC_MODEL, modelAndView.model)

    }

    private static void loadSiteMetadata(Site site, ModelAndView mv) {
        if (site != null && mv != null) {
            mv.addObject("siteKey", site.key)
            mv.addObject("site", site)
            mv.addObject("metaAuthor", site.metadataAuthor)
            mv.addObject("metaRights", site.metadataRights)
            if (!mv.model.containsKey("metaDescription")) {
                mv.addObject("metaDescription", site.metadataDescription)
            }
            if (!mv.model.containsKey("metaKeywords")) {
                mv.addObject("metaKeywords", site.metadataKeywords)
            }

        }
    }

    static void shutdown(Site site, ModelAndView mv) {
        mv.viewName = "error/offline"
        mv.addObject("title", "OFFLINE!")
        mv.addObject("site", site)
        mv.addObject("siteKey", site.key)
        mv.addObject("offlineIcon", site.offlineIcon)
        mv.addObject("offlineMessage", site.offlineMessage)

    }

}
