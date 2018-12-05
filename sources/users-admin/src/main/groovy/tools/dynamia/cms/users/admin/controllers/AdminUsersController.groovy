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
package tools.dynamia.cms.users.admin.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.users.admin.Initializer
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
class AdminUsersController {

    @Autowired
    private SiteService service

    @RequestMapping("/login")
    ModelAndView login(HttpServletRequest request) {
        Initializer initializer = Containers.get().findObject(Initializer.class)
        initializer.init(request)

        Site site = service.getSite(request)
        SiteContext.get().current = site

        ModelAndView mv = new ModelAndView("login")
        mv.addObject("contextPath", request.contextPath)

        return mv
    }

    @RequestMapping(value = "/cms-admin/logout", method = [RequestMethod.GET, RequestMethod.POST])
    ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.context.authentication
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth)
        }
        ModelAndView mv = new ModelAndView()
        mv.view = new RedirectView("/", false, true, false)
        return mv
    }

}
