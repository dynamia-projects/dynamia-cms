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
package tools.dynamia.cms.site.pages.controllers;

import tools.dynamia.cms.site.core.actions.SiteActionManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.pages.PageContext;
import tools.dynamia.cms.site.pages.PageNotFoundException;
import tools.dynamia.cms.site.pages.api.PageTypeExtension;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.cms.site.pages.domain.PageParameter;
import tools.dynamia.cms.site.pages.services.PageService;
import tools.dynamia.commons.DateInfo;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.ObjectMatcher;

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
public class PageController {

    @Autowired
    private PageService service;
    @Autowired
    private SiteService coreService;

    private LoggingService logger = new SLF4JLoggingService(PageController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request) {
        logger.debug("Loading site home page");
        return page("index", request);
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String page, HttpServletRequest request) {
        return getPage(page, request);
    }

    @RequestMapping(value = "/pages/{year}", method = RequestMethod.GET)
    public ModelAndView pagesByYear(@PathVariable int year, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, 0, 0));
        return mv;
    }

    @RequestMapping(value = "/pages/{year}/{month}", method = RequestMethod.GET)
    public ModelAndView pagesByMonth(@PathVariable int year, @PathVariable int month, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, month, 0));
        return mv;
    }

    @RequestMapping(value = "/pages/{year}/{month}/{day}", method = RequestMethod.GET)
    public ModelAndView pagesByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        SiteActionManager.performAction("loadPagesByDate", mv, request, new DateInfo(year, month, day));
        return mv;
    }

    @RequestMapping(value = "/category/{category}", method = RequestMethod.GET)
    public ModelAndView pagesByCategory(@PathVariable String category, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        SiteActionManager.performAction("loadPagesByCategory", mv, request, category);
        return mv;
    }

    private ModelAndView getPage(String pageAlias, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("site/page");

        Site site = coreService.getSite(request);

        Page page = loadPage(site, pageAlias, mv);
        configurePageType(page, site, mv, request);

        return mv;
    }

    private Page loadPage(Site site, String pageAlias, ModelAndView mv) {
        Page page = null;
        if (site != null) {
            page = service.loadPage(site, pageAlias);

            if (page == null) {
                logger.debug("Page not found [" + pageAlias + "] in site " + site);
                throw new PageNotFoundException(pageAlias, site.getKey());
            }

            mv.addObject("page", page);
            mv.addObject("pageImage", page.getImageURL());
            mv.addObject("title", page.getTitle());
            mv.addObject("subtitle", page.getSubtitle());
            mv.addObject("icon", page.getIcon());

            if (page.getSummary() != null && !page.getSummary().isEmpty()) {
                mv.addObject("metaDescription", page.getSummary());
            }

            if (page.getTags() != null && !page.getTags().isEmpty()) {
                mv.addObject("metaKeywords", page.getTags());
            }

            if (page.getParameters() != null) {
                Map<String, Object> pageParams = new HashMap<String, Object>();
                for (PageParameter param : page.getParameters()) {
                    if (param.isEnabled()) {
                        pageParams.put(param.getName(), param.getValue());
                    }
                }
                mv.addObject("pageParams", pageParams);
            }

        }
        return page;
    }

    private PageTypeExtension getExtension(final String type) {
        Collection<PageTypeExtension> types = Containers.get().findObjects(PageTypeExtension.class, new ObjectMatcher<PageTypeExtension>() {

            @Override
            public boolean match(PageTypeExtension object) {
                return object.getId().equals(type);
            }
        });

        for (PageTypeExtension pageTypeExtension : types) {
            return pageTypeExtension;
        }

        return null;
    }

    private void configurePageType(Page page, Site site, ModelAndView mv, HttpServletRequest request) {
        if (page != null) {
            String type = page.getType();
            PageTypeExtension pageTypeExt = getExtension(type);
            if (pageTypeExt != null) {
                PageContext context = new PageContext(page, site, mv, request);
                pageTypeExt.setupPage(context);
            }
        }
    }
}
