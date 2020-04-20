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
package tools.dynamia.cms.core.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.View
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
class SiteController {

    @Autowired
    private SiteService service

    @ResponseBody
    @RequestMapping("/google{siteid}.html")
    String googleSiteVerification(@PathVariable String siteid, HttpServletRequest request) {
        Site site = service.getSite(request)
        if (site == null) {
            site = service.mainSite
        }

        String fullId = "google${siteid}.html"
        if (fullId != site.googleSiteVerification) {
            fullId = "unknown"
        }
        return "google-site-verification: $fullId"

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    View redirectIndex() {
        return new RedirectView("/", true, true, false)
    }
}
