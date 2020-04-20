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
package tools.dynamia.cms.users.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.core.api.SiteRequestInterceptorAdapter
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.services.UserService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class UserCustomersInterceptor extends SiteRequestInterceptorAdapter {

	@Autowired
	private UserService service

    @Override
	void afterRequest(Site site, ModelAndView modelAndView) {
		if (UserHolder.get().seller && modelAndView.modelMap.get("userCustomers") == null) {
			modelAndView.addObject("userCustomers", service.getUserCustomers(UserHolder.get().current))
        }
	}

}
