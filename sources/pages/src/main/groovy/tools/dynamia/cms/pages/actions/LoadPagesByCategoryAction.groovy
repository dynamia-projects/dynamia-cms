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
package tools.dynamia.cms.pages.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageCategory
import tools.dynamia.cms.pages.services.PageService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class LoadPagesByCategoryAction implements SiteAction {

    @Autowired
    private PageService service

    @Override
    String getName() {
        return "loadPagesByCategory"
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        ModelAndView mv = evt.modelAndView
        mv.viewName = "site/pagelist"

        String categoryAlias = (String) evt.data
        if (categoryAlias == null) {
            return
        }

        PageCategory pageCategory = service.getPageCategoryByAlias(evt.site, categoryAlias)
        if (pageCategory != null) {
            mv.addObject("title", pageCategory.name)

            List<Page> pages = service.getPages(evt.site, pageCategory)
            pages = CMSUtil.setupPagination(pages, evt.request, mv)

            mv.addObject("pages", pages)

            if (pages.empty) {
                mv.view = new RedirectView("/", true, true, false)
            }
        }

    }

}
