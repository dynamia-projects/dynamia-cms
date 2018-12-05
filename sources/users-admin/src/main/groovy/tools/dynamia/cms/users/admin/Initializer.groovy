/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.users.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.integration.sterotypes.Component

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
@Component
@Scope("session")
class Initializer {

	@Autowired
	private UserService userService
    @Autowired
	private SiteService siteService

    void init(HttpServletRequest request) {

		if (userService != null && request != null && siteService != null) {
			Site site = siteService.getSite(request)
            if (site != null) {
				userService.checkAdminUser(site)

                if ("main".equals(site.key)) {
					DynamiaCMS.initDefaultLocations()
                }
			}

		
		}
	}

}
