/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.CoreService;
import com.dynamia.cms.site.pages.PageControllerListener;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.services.PageService;
import com.dynamia.tools.integration.Containers;
import java.util.Collection;
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
    private CoreService coreService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(Model model) {

        return page(null, model);
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String page, Model model) {
        return page("main", page, model);
    }

    @RequestMapping(value = "/site/{siteKey}/{pageAlias}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String siteKey, @PathVariable String pageAlias, Model model) {
        if (pageAlias == null) {
            pageAlias = "index";
        }

        ModelAndView mv = new ModelAndView("site/page");

        Site site = loadSite(siteKey, mv);
        Page page = loadPage(site, pageAlias, mv);
        firePageControllerListeners(page, mv);
        return mv;
    }

    private Site loadSite(String siteKey, ModelAndView mv) {
        Site site = coreService.getSite(siteKey);
        mv.addObject("site", site);

        if (site == null) {
            if (siteKey.equals("main")) {
                mv.setViewName("site/welcome");
            } else {
                mv.setViewName("error/404site");
                mv.addObject("siteKey", siteKey);
            }
        }
        return site;

    }

    private Page loadPage(Site site, String pageAlias, ModelAndView mv) {
        Page page = null;
        if (site != null) {
            page = service.loadPage(site, pageAlias);
            mv.addObject("page", page);

            if (page == null) {
                mv.setViewName("error/404");
                mv.addObject("pageAlias", pageAlias);
            }
        }
        return page;
    }

    private void firePageControllerListeners(Page page, ModelAndView mv) {
        if (page != null && mv != null) {
            Collection<PageControllerListener> listeners = Containers.get().findObjects(PageControllerListener.class);
            for (PageControllerListener listener : listeners) {
                listener.onPageLoaded(page, mv);
            }
        }
    }
}
