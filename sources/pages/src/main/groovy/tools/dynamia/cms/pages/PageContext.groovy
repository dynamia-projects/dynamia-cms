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
package tools.dynamia.cms.pages

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.pages.domain.PageParameter

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Mario Serrano Leones
 */
class PageContext implements Serializable {

    private Page page
    private Site site
    private HttpServletRequest request
    private ModelAndView modelAndView
    private List<PageParameter> parameters

    PageContext(Page page, Site site, ModelAndView modelAndView, HttpServletRequest request) {
        this.page = page
        this.site = site
        this.modelAndView = modelAndView
        this.request = request
        if (page != null) {
            this.parameters = page.parameters
        }
    }

    Site getSite() {
        return site
    }

    Page getPage() {
        return page
    }

    ModelAndView getModelAndView() {
        return modelAndView
    }

    HttpServletRequest getRequest() {
        return request
    }

    PageParameter getParameter(String name) {
        if (parameters != null) {
            for (PageParameter param : parameters) {
                if (param.name.equals(name)) {
                    return param
                }
            }
        }
        return null
    }

}
