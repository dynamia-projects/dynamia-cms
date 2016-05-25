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
package tools.dynamia.cms.site.pages;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.pages.domain.Page;
import tools.dynamia.cms.site.pages.domain.PageParameter;

/**
 *
 * @author Mario Serrano Leones
 */
public class PageContext implements Serializable {

    private Page page;
    private Site site;
    private HttpServletRequest request;
    private ModelAndView modelAndView;
    private List<PageParameter> parameters;

    public PageContext(Page page, Site site, ModelAndView modelAndView, HttpServletRequest request) {
        this.page = page;
        this.site = site;
        this.modelAndView = modelAndView;
        this.request = request;
        if (page != null) {
            this.parameters = page.getParameters();
        }
    }

    public Site getSite() {
        return site;
    }

    public Page getPage() {
        return page;
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public PageParameter getParameter(String name) {
        if (parameters != null) {
            for (PageParameter param : parameters) {
                if (param.getName().equals(name)) {
                    return param;
                }
            }
        }
        return null;
    }

}
