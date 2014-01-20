/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageParameter;
import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
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
