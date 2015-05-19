/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.controllers;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.domain.UserContactInfo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author mario_2
 */
@Controller
@RequestMapping("/users")
public class UsersController {

	@RequestMapping(value = { "/login", "/save" }, method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/login");
		mv.addObject("title", "Inicio de Sesion");
		SiteActionManager.performAction("loginUser", mv, request);

		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveUser(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("users/save");
		mv.addObject("title", "Registro de Usuarios");
		SiteActionManager.performAction("saveUser", mv, request, user);

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public ModelAndView account(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/account");
		mv.addObject("title", "Mi Cuenta");
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public ModelAndView changePassword(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/changepassword");
		mv.addObject("title", "Cambiar Password");
		SiteActionManager.performAction("updateUser", mv, request);
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public ModelAndView changePassword(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("users/accounts");
		SiteActionManager.performAction("changePassword", mv, request, redirectAttributes, user);
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/profile");
		mv.addObject("title", "Mis Datos");

		SiteActionManager.performAction("updateUser", mv, request);

		mv.addObject("redirect", request.getParameter("redirect"));
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView profile(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("users/account");
		SiteActionManager.performAction("saveUserProfile", mv, request, redirectAttributes, user);

		if (request.getParameter("redirect") != null) {
			mv.setView(new RedirectView(request.getParameter("redirect"), true, true, false));
		}

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	public ModelAndView addresses(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/addresses/table");

		SiteActionManager.performAction("showUserContactInfos", mv, request);
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addresses", method = RequestMethod.POST)
	public ModelAndView saveAddress(@Valid UserContactInfo userContactInfo, BindingResult bindingResult, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		String redirect = request.getParameter("currentURI");
		if (request.getParameter("redirect") != null) {
			redirect = request.getParameter("redirect");
		}
		mv.setView(new RedirectView(redirect, true, true, false));
		SiteActionManager.performAction("saveUserContactInfo", mv, request, redirectAttributes, userContactInfo);

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addresses/add", method = RequestMethod.GET)
	public ModelAndView addAddress(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/addresses/form");
		SiteActionManager.performAction("addUserContactInfo", mv, request);

		mv.addObject("redirect", request.getParameter("redirect"));

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addresses/{id}/edit", method = RequestMethod.GET)
	public ModelAndView editAddress(@PathVariable Long id, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/addresses/form");
		SiteActionManager.performAction("editUserContactInfo", mv, request, id);
		mv.addObject("redirect", request.getParameter("redirect"));
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addresses/{id}/remove", method = RequestMethod.GET)
	public ModelAndView removeAddress(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("users/addresses/table");
		mv.setView(new RedirectView("/users/profile", true, true, false));
		SiteActionManager.performAction("removeUserContactInfo", mv, request, redirectAttributes, id);
		return mv;
	}

}
