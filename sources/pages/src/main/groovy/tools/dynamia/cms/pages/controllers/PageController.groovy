/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.pages.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.SiteHandleInterceptor
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.pages.PageContext
import tools.dynamia.cms.pages.PageNotFoundException
import tools.dynamia.cms.pages.api.PageTypeExtension
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageParameter
import tools.dynamia.cms.pages.services.PageService
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

        Page page = loadPage(site, pageAlias, mv)

        configurePageType(page, site, mv, request)

        return mv
    }

    private Page loadPage(Site site, String pageAlias, ModelAndView mv) {
        pageAlias = fixAlias(pageAlias)


        Page page = null
        if (site != null) {
            page = service.loadPage(site, pageAlias)

            if (page == null) {
                logger.debug("Page not found [$pageAlias] in site $site")
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

    String fixAlias(String alias) {
        if (alias == "login" || alias == "null") {
            alias = "index"
        }
        return alias
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

            if (page.layout != null && !page.layout.empty) {
                mv.viewName = "site/layouts/${page.layout}"
            }
        }
    }

}
