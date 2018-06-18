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