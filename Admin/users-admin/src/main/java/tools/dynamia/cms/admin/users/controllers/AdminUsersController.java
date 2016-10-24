/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.users.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView("/", false, true, false));
		return mv;
	}

}
