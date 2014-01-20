/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.actions;

import com.dynamia.cms.site.core.domain.Site;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public class ActionEvent implements Serializable {

    private Site site;
    private ModelAndView modelAndView;
    private HttpServletRequest request;
    private Object source;
    private Object data;

    ActionEvent(Site site, ModelAndView modelAndView, HttpServletRequest request, Object source, Object data) {
        this.site = site;
        this.modelAndView = modelAndView;
        this.request = request;
        this.source = source;
        this.data = data;
    }

    public Site getSite() {
        return site;
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public Object getSource() {
        return source;
    }

    public Object getData() {
        return data;
    }

}
