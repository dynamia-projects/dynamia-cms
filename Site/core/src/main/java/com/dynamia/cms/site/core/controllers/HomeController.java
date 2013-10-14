/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mario
 */
@Controller
public class HomeController {

    public HomeController() {
        System.out.println(">>>>> INICIANDO "+getClass().getName());
    }
    
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homeForm(Model model) {


        model.addAttribute("name", "DynamiaCMS Site");

        return "index";
    }
}
