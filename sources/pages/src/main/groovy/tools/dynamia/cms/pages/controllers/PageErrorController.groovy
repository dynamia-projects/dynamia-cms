/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.pages.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.SiteRequestInterceptor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
@ControllerAdvice
class PageErrorController {

    @Autowired
    private SiteService siteService

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(tools.dynamia.cms.pages.PageNotFoundException.class)
    @RequestMapping("/404")
    ModelAndView handleException(HttpServletRequest request, HttpServletResponse response,
                                 tools.dynamia.cms.pages.PageNotFoundException exception) {
        ModelAndView mv = new ModelAndView("error/404")
        mv.addObject("exception", exception)
        if (exception.pageAlias != null) {
            mv.addObject("pageAlias", exception.pageAlias)
            mv.addObject("siteKey", exception.siteKey)
        } else {
            mv.addObject("pageAlias", request.requestURI)
            mv.addObject("siteKey", request.serverName)
        }

        Site site = siteService.getSite(request)
        Containers.get().findObjects(SiteRequestInterceptor.class).forEach { i ->
            i.afterRequest(site, request, response, mv)
        }

        return mv
    }

}
