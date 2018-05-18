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
