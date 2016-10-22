/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.users.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import tools.dynamia.cms.site.core.actions.SiteActionManager;
import tools.dynamia.cms.site.users.UserForm;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.UserContactInfo;

/**
 *
 * @author Mario Serrano Leones
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

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveUser(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView("users/save");
		mv.addObject("title", "Registro de Usuarios");
		SiteActionManager.performAction("saveUser", mv, request, user);

		return mv;
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("users/resetpassword");
		mv.addObject("title", "Olvide mi Password");
		UserForm form = new UserForm();
		form.setData(new User());
		mv.addObject("userForm", form);
		return mv;
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("users/resetpassword");
		mv.addObject("title", "Olvide mi Password");
		SiteActionManager.performAction("resetPassword", mv, request, redirectAttributes, user);
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
	public ModelAndView saveAddress(@Valid UserContactInfo userContactInfo, BindingResult bindingResult,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		String redirect = request.getParameter("currentURI");
		if (request.getParameter("redirect") != null) {
			redirect = request.getParameter("redirect");
		}
		mv.addObject("redirect", redirect);

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
	public ModelAndView removeAddress(@PathVariable Long id, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("users/addresses/table");
		mv.setView(new RedirectView("/users/profile", true, true, false));
		SiteActionManager.performAction("removeUserContactInfo", mv, request, redirectAttributes, id);
		return mv;
	}

}
