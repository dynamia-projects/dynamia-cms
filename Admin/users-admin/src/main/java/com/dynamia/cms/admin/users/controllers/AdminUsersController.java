/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.users.controllers;

import com.dynamia.cms.admin.core.SiteHolder;
import com.dynamia.cms.admin.users.Initializer;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario_2
 */
@Controller
public class AdminUsersController {

    @Autowired
    private SiteService service;

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        Initializer initializer = Containers.get().findObject(Initializer.class);
        initializer.init(request);

        Site site = service.getSite(request);
        SiteHolder.get().setCurrent(site);

        ModelAndView mv = new ModelAndView("login");

        return mv;
    }

}
