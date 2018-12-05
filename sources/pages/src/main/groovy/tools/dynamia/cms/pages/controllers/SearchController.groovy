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
