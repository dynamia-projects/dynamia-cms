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
