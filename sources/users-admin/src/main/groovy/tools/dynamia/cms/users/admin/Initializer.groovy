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
