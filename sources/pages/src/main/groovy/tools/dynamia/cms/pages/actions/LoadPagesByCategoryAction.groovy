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
