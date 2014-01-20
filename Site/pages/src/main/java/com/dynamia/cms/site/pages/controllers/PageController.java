/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.pages.PageNotFoundException;
import com.dynamia.cms.site.pages.SearchForm;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.cms.site.pages.services.PageService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.ObjectMatcher;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
public class PageController {

    @Autowired
    private PageService service;
    @Autowired
    private SiteService coreService;

    private LoggingService logger = new SLF4JLoggingService(PageController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(Model model, HttpServletRequest request) {
        logger.debug("Loading site home page");
        return page("index", model, request);
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String page, Model model, HttpServletRequest request) {

        String domain = request.getServerName();
        logger.debug("Loading page [" + page + "] for domain [" + domain + "]");

        return page(domain, page, model);
    }

    private ModelAndView page(String domainName, String pageAlias, Model model) {

        ModelAndView mv = new ModelAndView("site/page");

        Site site = loadSite(domainName, mv);
        loadPage(site, pageAlias, mv);

        return mv;
    }

    private Site loadSite(String domainName, ModelAndView mv) {
        Site site = coreService.getSite(domainName);
        if (site == null) {
            logger.info("Site for domain [" + domainName + "] not found. Using main site");
            site = coreService.getMainSite();
        }

        mv.addObject("site", site);

        if (site == null) {
            logger.info("Main site is not configured");
            mv.setViewName("error/404site");
        } else {
            mv.addObject("siteKey", site.getKey());
            mv.addObject("domainName", domainName);
        }
        return site;

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
            mv.addObject("title", page.getTitle());
            mv.addObject("subtitle", page.getSubtitle());
            mv.addObject("icon", page.getIcon());
            String type = page.getType();
            PageTypeExtension pageTypeExt = getExtension(type);
            if (pageTypeExt != null) {
                Map<String, Object> params = pageTypeExt.setupPage(page);
                mv.addAllObjects(params);
                mv.setViewName(pageTypeExt.getViewName());
            }

        }
        return page;
    }

    private PageTypeExtension getExtension(final String type) {
        Collection<PageTypeExtension> types = Containers.get().findObjects(PageTypeExtension.class, new ObjectMatcher<PageTypeExtension>() {

            @Override
            public boolean match(PageTypeExtension object) {
                return object.getName().equals(type);
            }
        });

        for (PageTypeExtension pageTypeExtension : types) {
            return pageTypeExtension;
        }

        return null;
    }
}
