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
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.commons.DateInfo
import tools.dynamia.commons.DateRange
import tools.dynamia.commons.collect.CollectionsUtils

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class LoadPagesByDateAction implements SiteAction {

    @Autowired
    private tools.dynamia.cms.pages.services.PageService service

    @Override
    String getName() {
        return "loadPagesByDate"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        DateInfo dateInfo = (DateInfo) evt.data
        DateRange dateRange = dateInfo.toRange()

        ModelAndView mv = evt.modelAndView
        mv.viewName = "site/pagelist"

        List<tools.dynamia.cms.pages.domain.Page> pages = service.findPagesByDate(evt.site, dateRange.startDate, dateRange.endDate)
        mv.addObject("pages", pages)

        if (pages.empty) {
            mv.view = new RedirectView("/", true, true, false)
        } else {
            mv.addObject("title", CollectionsUtils.findFirst(pages).title)
        }

    }

}
