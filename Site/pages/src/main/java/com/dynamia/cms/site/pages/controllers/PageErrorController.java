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
package com.dynamia.cms.site.pages.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.pages.PageNotFoundException;

/**
 *
 * @author Mario Serrano Leones
 */
public class PageErrorController {

    @ExceptionHandler(PageNotFoundException.class)
    public ModelAndView handleException(HttpServletRequest request, PageNotFoundException exception) {
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("exception", exception);
        if (exception.getPageAlias() != null) {
            mv.addObject("pageAlias", exception.getPageAlias());
            mv.addObject("siteKey", exception.getSiteKey());
        } else {
            mv.addObject("pageAlias", request.getPathInfo());
            mv.addObject("siteKey", request.getServerName());
        }

        return mv;
    }

}
