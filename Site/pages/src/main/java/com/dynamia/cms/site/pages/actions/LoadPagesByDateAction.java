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
package com.dynamia.cms.site.pages.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.services.PageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import tools.dynamia.commons.CollectionsUtils;
import tools.dynamia.commons.DateInfo;
import tools.dynamia.commons.DateRange;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class LoadPagesByDateAction implements SiteAction {

    @Autowired
    private PageService service;

    @Override
    public String getName() {
        return "loadPagesByDate";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        DateInfo dateInfo = (DateInfo) evt.getData();
        DateRange dateRange = dateInfo.toRange();

        ModelAndView mv = evt.getModelAndView();
        mv.setViewName("site/pagelist");

        List<Page> pages = service.findPagesByDate(evt.getSite(), dateRange.getStartDate(), dateRange.getEndDate());
        mv.addObject("pages", pages);

        if (pages.isEmpty()) {
            mv.setView(new RedirectView("/", true, true, false));            
        } else {
            mv.addObject("title", CollectionsUtils.findFirst(pages).getTitle());
        }

    }

}
