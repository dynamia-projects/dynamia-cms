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
package tools.dynamia.cms.site.core.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.CMSException;
import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.api.SiteRequestInterceptor;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
@Controller	
public class ExceptionsController {

    private LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class);

    @Autowired
    private SiteService siteService;

    @RequestMapping("/exception")
    @ExceptionHandler(Exception.class)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        if (throwable == null) {
            throwable = exception;
        }

        if (exception instanceof CMSException) {
            statusCode = ((CMSException) exception).getStatus().value();
        }

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
      
        if (requestUri == null) {
            requestUri = request.getRequestURI()+ "";
        }

        logger.error("ERROR " + statusCode + ": " + requestUri + " on  " + request.getServerName(), throwable);

        ModelAndView mv = new ModelAndView("error/exception");
        if (statusCode != null && statusCode == 404) {
            mv.setViewName("error/404");
            mv.addObject("pageAlias", requestUri);
        }
        mv.addObject("title", "Error");
        mv.addObject("statusCode", statusCode);
        mv.addObject("uri", requestUri);
        mv.addObject("exception", throwable);
        Site site = siteService.getSite(request);
        Containers.get().findObjects(SiteRequestInterceptor.class).forEach(i -> {
            i.afterRequest(site, request, response, mv);
        });
        return mv;
    }

}
