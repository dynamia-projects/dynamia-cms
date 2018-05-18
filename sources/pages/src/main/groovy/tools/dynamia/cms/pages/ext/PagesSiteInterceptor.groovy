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
