/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.pages.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CMSExtension
@Order(Integer.MAX_VALUE)
class PagesSiteInterceptor implements SiteRequestInterceptor {

	@Autowired
	private tools.dynamia.cms.pages.services.PageService service

    @Override
    void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
    void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response,
                      ModelAndView modelAndView) {
		Page page = (Page) modelAndView.model.get("page")
        if (page == null) {
			page = new Page()
            page.site = site
            page.alias = SiteContext.get().currentURI
            page.title = (String) modelAndView.model.get("title")
            page.subtitle = (String) modelAndView.model.get("subtitle")
            page.icon = (String) modelAndView.model.get("icon")
            page.type = "auto"

            modelAndView.addObject("page", page)
            modelAndView.addObject("pageContent", page.content)
        } else if (page.templateEngine != null && modelAndView.model.containsKey("pageContent")) {
			modelAndView.addObject("pageContent", service.parsePageContent(page, modelAndView.model))
        }

	}

}
