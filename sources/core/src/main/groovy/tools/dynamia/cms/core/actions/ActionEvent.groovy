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
package tools.dynamia.cms.core.actions

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import tools.dynamia.cms.core.domain.Site

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
