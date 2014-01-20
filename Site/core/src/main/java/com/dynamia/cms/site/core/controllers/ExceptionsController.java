/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.controllers;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
public class ExceptionsController {

    private LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class);

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        logger.error("ERROR " + statusCode + ": " + requestUri+" on  "+request.getServerName(), throwable);

        ModelAndView mv = new ModelAndView("error/exception");

        mv.addObject("title", "Error");
        mv.addObject("statusCode", statusCode);
        mv.addObject("uri", requestUri);
        mv.addObject("exception", throwable);

        switch (statusCode) {
            case 404:
                mv.setViewName("error/404");
                mv.addObject("pageAlias", requestUri);
                break;
        }

        return mv;

    }

}
