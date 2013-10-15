/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.controllers;

import com.dynamia.cms.site.pages.PageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
public class PageController {

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String page, Model model) {

        ModelAndView mv = new ModelAndView("index");
        mv.addObject("name", "DynamiaCMS Site - "+page);

        PageRepository ds = new PageRepository();
        mv.addObject("page", ds.loadPage(page));

        return mv;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(Model model) {

        ModelAndView mv = new ModelAndView("index");

        mv.addObject("name", "DynamiaCMS Site");


        PageRepository ds = new PageRepository();
        mv.addObject("page", ds.loadPage("CMS"));

        return mv;
    }
}
