/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.pages.SearchForm;
import com.dynamia.cms.site.pages.api.SearchProvider;
import com.dynamia.cms.site.pages.api.SearchResult;

import tools.dynamia.integration.Containers;

/**
 *
 * @author mario
 */
@Controller
public class SearchController {

    @Autowired
    private SiteService siteService;

    @RequestMapping("/search")
    public ModelAndView search(@Valid SearchForm form, BindingResult bindingResult, final HttpServletRequest request) {
        Site site = siteService.getSite(request);

        ModelAndView mv = new ModelAndView("site/page");
        mv.addObject("searchForm", form);
        form.setRequest(request);
        Collection<SearchProvider> providers = Containers.get().findObjects(SearchProvider.class);
        for (SearchProvider searchProvider : providers) {
            SearchResult result = searchProvider.search(site, form);
            if (result != null) {
                mv.addAllObjects(result.getEntries());
                mv.setViewName(result.getViewName());
                if (!result.isKeepSearching()) {
                    break;
                }
            }
        }
        return mv;
    }

}
