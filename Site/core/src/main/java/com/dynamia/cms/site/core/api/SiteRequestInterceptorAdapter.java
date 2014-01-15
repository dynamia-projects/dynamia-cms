/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.api;

import com.dynamia.cms.site.core.domain.Site;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public class SiteRequestInterceptorAdapter implements SiteRequestInterceptor {

    @Override
    public void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response) {
        beforeRequest(site);
    }

    @Override
    public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        afterRequest(site, modelAndView);
    }

    protected void beforeRequest(Site site) {
    }

    protected void afterRequest(Site site, ModelAndView modelAndView) {
    }

}
