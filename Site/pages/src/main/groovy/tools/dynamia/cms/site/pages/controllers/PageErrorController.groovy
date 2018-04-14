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
package tools.dynamia.cms.site.pages.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.api.SiteRequestInterceptor
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.cms.site.pages.PageNotFoundException
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
    @ExceptionHandler(PageNotFoundException.class)
    @RequestMapping("/404")
    ModelAndView handleException(HttpServletRequest request, HttpServletResponse response,
                                 PageNotFoundException exception) {
        ModelAndView mv = new ModelAndView("error/404")
        mv.addObject("exception", exception)
        if (exception.getPageAlias() != null) {
            mv.addObject("pageAlias", exception.getPageAlias())
            mv.addObject("siteKey", exception.getSiteKey())
        } else {
            mv.addObject("pageAlias", request.getPathInfo())
            mv.addObject("siteKey", request.getServerName())
        }

        Site site = siteService.getSite(request)
        Containers.get().findObjects(SiteRequestInterceptor.class).forEach { i ->
            i.afterRequest(site, request, response, mv)
        }

        return mv
    }

}
