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
	void afterRequest(Site site, ModelAndView mv) {
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
