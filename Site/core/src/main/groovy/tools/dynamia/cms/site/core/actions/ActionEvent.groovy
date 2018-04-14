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
package tools.dynamia.cms.site.core.actions

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import tools.dynamia.cms.site.core.domain.Site

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Mario Serrano Leones
 */
class ActionEvent implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6809369433426936140L
    private Site site
    private ModelAndView modelAndView
    private HttpServletRequest request
    private HttpServletResponse response
    private RedirectAttributes redirectAttributes
    private Object source
    private Object data


    ActionEvent(Site site, ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Object source, Object data) {
        this.site = site
        this.modelAndView = modelAndView
        this.request = request
        this.response = response
        this.redirectAttributes = redirectAttributes
        this.source = source
        this.data = data
    }

    RedirectAttributes getRedirectAttributes() {
        return redirectAttributes
    }

    Site getSite() {
        return site
    }

    ModelAndView getModelAndView() {
        return modelAndView
    }

    HttpServletRequest getRequest() {
        return request
    }

    HttpServletResponse getResponse() {
        return response
    }

    Object getSource() {
        return source
    }

    Object getData() {
        return data
    }
   
}
