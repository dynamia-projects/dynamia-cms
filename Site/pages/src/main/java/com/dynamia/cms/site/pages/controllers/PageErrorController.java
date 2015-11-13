/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.pages.PageNotFoundException;

/**
 *
 * @author mario
 */
public class PageErrorController {

    @ExceptionHandler(PageNotFoundException.class)
    public ModelAndView handleException(HttpServletRequest request, PageNotFoundException exception) {
        ModelAndView mv = new ModelAndView("error/404");
        mv.addObject("exception", exception);
        if (exception.getPageAlias() != null) {
            mv.addObject("pageAlias", exception.getPageAlias());
            mv.addObject("siteKey", exception.getSiteKey());
        } else {
            mv.addObject("pageAlias", request.getPathInfo());
            mv.addObject("siteKey", request.getServerName());
        }

        return mv;
    }

}
