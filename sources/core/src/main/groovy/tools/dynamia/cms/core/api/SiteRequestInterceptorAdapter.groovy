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
package tools.dynamia.cms.core.api

import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
class SiteRequestInterceptorAdapter implements SiteRequestInterceptor {

    @Override
    void beforeRequest(tools.dynamia.cms.core.domain.Site site, HttpServletRequest request, HttpServletResponse response) {
        beforeRequest(site)
    }

    @Override
    void afterRequest(tools.dynamia.cms.core.domain.Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        afterRequest(site, modelAndView)
    }

    protected void beforeRequest(tools.dynamia.cms.core.domain.Site site) {
    }

    protected void afterRequest(tools.dynamia.cms.core.domain.Site site, ModelAndView modelAndView) {
    }

}
