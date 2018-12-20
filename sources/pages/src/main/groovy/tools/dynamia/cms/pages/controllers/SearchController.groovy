/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.pages.SearchForm
import tools.dynamia.cms.pages.api.SearchProvider
import tools.dynamia.cms.pages.api.SearchResult
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
class SearchController {

    @Autowired
    private SiteService siteService

    @RequestMapping("/search")
    ModelAndView search(@Valid SearchForm form, BindingResult bindingResult, final HttpServletRequest request) {
        Site site = siteService.getSite(request)

        ModelAndView mv = new ModelAndView("site/page")
        mv.addObject("searchForm", form)
        form.request = request
        Collection<SearchProvider> providers = Containers.get().findObjects(SearchProvider.class)
        for (SearchProvider searchProvider : providers) {
            SearchResult result = searchProvider.search(site, form)
            if (result != null) {
                mv.addAllObjects(result.entries)
                mv.viewName = result.viewName
                if (!result.keepSearching) {
                    break
                }
            }
        }
        return mv
    }

}
