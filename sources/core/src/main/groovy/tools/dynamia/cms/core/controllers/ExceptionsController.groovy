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
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSException
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.api.SiteRequestInterceptor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Mario Serrano Leones
 */
@Controller
class ExceptionsController implements ErrorController {

    private LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class)

    @Autowired
    private SiteService siteService

    @RequestMapping(["/exception", "/error"])
    @ExceptionHandler(Exception.class)
    ModelAndView error(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code")
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception")

        if (throwable == null) {
            throwable = exception
        }

        if (exception instanceof CMSException) {
            statusCode = ((CMSException) exception).status.value()
        }

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri")

        if (requestUri == null) {
            requestUri = request.requestURI + ""
        }

        if (statusCode != 404) {
            logger.error("ERROR " + statusCode + ": " + requestUri + " on  " + request.serverName, throwable)
        } else {
            logger.warn(String.format("Error 404 - Resource [%s] not found in site [%s]", requestUri, request.serverName))
        }
        ModelAndView mv = new ModelAndView("error/error")
        mv.setStatus(HttpStatus.valueOf(statusCode))
        if (statusCode != null && statusCode == 404) {
            mv.viewName = "error/404"
            mv.addObject("pageAlias", requestUri)
        }
        mv.addObject("title", "Error")
        mv.addObject("statusCode", statusCode)
        mv.addObject("uri", requestUri)
        mv.addObject("exception", throwable)
        mv.addObject("contextPath", request.contextPath)

        List<String> causes = new ArrayList<>()
        getCauses(causes, throwable.cause)
        mv.addObject("causes", causes)

        Site site = siteService.getSite(request)
        Containers.get().findObjects(SiteRequestInterceptor.class).forEach { i ->
            i.afterRequest(site, request, response, mv)
        }
        return mv
    }

    private void getCauses(List<String> causes, Throwable throwable) {

        if (throwable != null) {
            if (causes.size() <= 10) {
                causes.add(throwable.message)
            }

            if (throwable.cause != null) {
                getCauses(causes, throwable.cause)
            }
        }

    }

    @Override
    String getErrorPath() {
        return "/error"
    }
}
