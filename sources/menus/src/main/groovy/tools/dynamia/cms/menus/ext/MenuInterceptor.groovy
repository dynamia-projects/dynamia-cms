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
package tools.dynamia.cms.menus.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.menus.domain.Menu
import tools.dynamia.cms.menus.domain.MenuItem
import tools.dynamia.cms.menus.services.MenuService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class MenuInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private MenuService service

    @Override
	protected void afterRequest(Site site, ModelAndView mv) {
		Menu menu = service.getMainMenu(site)
        if (menu != null) {
			List<MenuItem> items = new ArrayList<>(menu.items)
            for (MenuItem menuItem : items) {
				service.setupMenuItem(menuItem)
            }

			mv.addObject("menu", menu)
            mv.addObject("menuitems", items)
        }
	}

}
