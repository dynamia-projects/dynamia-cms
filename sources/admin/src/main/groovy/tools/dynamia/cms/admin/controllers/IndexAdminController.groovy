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

package tools.dynamia.cms.admin.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.InternalResourceView
import tools.dynamia.app.ApplicationInfo
import tools.dynamia.zk.navigation.ZKNavigationManager

@Controller
class IndexAdminController {

    @Autowired
    private ApplicationInfo applicationInfo

    IndexAdminController() {
        println "Starting CMS Admin Controller"
    }

    @GetMapping("/cms-admin")
    @Secured(["ROLE_ADMIN", "ROLE_EDITOR"])
    ModelAndView cmsAdmin() {
        ZKNavigationManager.instance.autoSyncClientURL = false
        def mv = new ModelAndView()
        mv.view = new InternalResourceView("/zkau/web/templates/${applicationInfo.template.toLowerCase()}/views/index.zul")
        mv.addObject("contextPath", "/cms-admin")
        return mv
    }
}
