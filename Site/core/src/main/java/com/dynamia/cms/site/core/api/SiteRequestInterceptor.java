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
public interface SiteRequestInterceptor {

    public void beforeRequest(Site site, HttpServletRequest request, HttpServletResponse response);

    public void afterRequest(Site site, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

}
