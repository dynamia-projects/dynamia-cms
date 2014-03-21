/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.controllers;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.users.UserForm;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario_2
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    @RequestMapping(value = {"/login", "/save"}, method = RequestMethod.GET)
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
    public ModelAndView changePassword(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/accounts");
        SiteActionManager.performAction("changePassword", mv, request, user);
        return mv;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/profile");
        mv.addObject("title", "Mis Datos");
        SiteActionManager.performAction("updateUser", mv, request);
        return mv;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView profile(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("users/account");

        SiteActionManager.performAction("saveUserProfile", mv, request, user);

        return mv;
    }

}
