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
