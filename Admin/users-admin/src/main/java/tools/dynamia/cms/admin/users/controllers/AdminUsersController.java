/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.users.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.admin.users.Initializer;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
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
        SiteContext.get().setCurrent(site);

        ModelAndView mv = new ModelAndView("login");

        return mv;
    }

}
