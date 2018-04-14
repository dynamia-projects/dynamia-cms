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
package tools.dynamia.cms.site.pages.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.SiteHandleInterceptor
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.cms.site.pages.PageContext
import tools.dynamia.cms.site.pages.PageNotFoundException
import tools.dynamia.cms.site.pages.api.PageTypeExtension
import tools.dynamia.cms.site.pages.domain.Page
import tools.dynamia.cms.site.pages.domain.PageParameter
import tools.dynamia.cms.site.pages.services.PageService
import tools.dynamia.commons.DateInfo
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
class PageController {

    @Autowired
    private PageService service
    @Autowired
    private SiteService siteService

    private LoggingService logger = new SLF4JLoggingService(PageController.class)

    @RequestMapping(value = "/", method = RequestMethod.GET)
    ModelAndView home(HttpServletRequest request) {
        logger.debug("Loading site home page")
        return page("index", request)
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    ModelAndView page(@PathVariable String page, HttpServletRequest request) {
        return getPage(page, request)
    }

    @RequestMapping(value = "/pages/{year}", method = RequestMethod.GET)
    ModelAndView pagesByYear(@PathVariable int year, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, 0, 0))
        return mv
    }

    @RequestMapping(value = "/pages/{year}/{month}", method = RequestMethod.GET)
    ModelAndView pagesByMonth(@PathVariable int year, @PathVariable int month, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, month, 0))
        return mv
    }

    @RequestMapping(value = "/pages/{year}/{month}/{day}", method = RequestMethod.GET)
    ModelAndView pagesByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day,
                            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, month, day))
        return mv
    }

    @RequestMapping(value = "/category/{category}", method = RequestMethod.GET)
    ModelAndView pagesByCategory(@PathVariable String category, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView()
        SiteActionManager.performAction("loadPagesByCategory", mv, request, category)
        return mv
    }

    @RequestMapping(value = "/offline")
    ModelAndView offline(HttpServletRequest request) {
        Site site = siteService.getSite(request)
        ModelAndView mv = new ModelAndView()
        SiteHandleInterceptor.shutdown(site, mv)
        return mv
    }

    private ModelAndView getPage(String pageAlias, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("site/page")

        Site site = siteService.getSite(request)

        if (site != null && site.offline) {
            SiteHandleInterceptor.shutdown(site, mv)
        } else {
            Page page = loadPage(site, pageAlias, mv)
            configurePageType(page, site, mv, request)
        }
        return mv
    }

    private Page loadPage(Site site, String pageAlias, ModelAndView mv) {
        Page page = null
        if (site != null) {
            page = service.loadPage(site, pageAlias)

            if (page == null) {
                logger.debug("Page not found [" + pageAlias + "] in site " + site)
                throw new PageNotFoundException(pageAlias, site.key)
            }

            mv.addObject("page", page)
            mv.addObject("pageContent", page.content)
            mv.addObject("pageImage", page.imageURL)
            mv.addObject("title", page.title)
            mv.addObject("subtitle", page.subtitle)
            mv.addObject("icon", page.icon)

            if (page.summary != null && !page.summary.empty) {
                mv.addObject("metaDescription", page.summary)
            }

            if (page.tags != null && !page.tags.empty) {
                mv.addObject("metaKeywords", page.tags)
            }

            if (page.parameters != null) {
                Map<String, Object> pageParams = new HashMap<String, Object>()
                for (PageParameter param : (page.parameters)) {
                    if (param.enabled) {
                        pageParams.put(param.name, param.value)
                    }
                }
                mv.addObject("pageParams", pageParams)
            }

        }
        return page
    }

    private PageTypeExtension getExtension(final String type) {
        Collection<PageTypeExtension> types = Containers.get().findObjects(PageTypeExtension.class,
                { object -> (object.id == type) })

        for (PageTypeExtension pageTypeExtension : types) {
            return pageTypeExtension
        }

        return null
    }

    private void configurePageType(Page page, Site site, ModelAndView mv, HttpServletRequest request) {
        if (page != null) {
            String type = page.type
            PageTypeExtension pageTypeExt = getExtension(type)
            if (pageTypeExt != null) {
                PageContext context = new PageContext(page, site, mv, request)
                pageTypeExt.setupPage(context)
            }
        }
    }

}
