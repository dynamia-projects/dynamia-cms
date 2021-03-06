/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.users.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.savedrequest.DefaultSavedRequest
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.users.UserForm
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.cms.users.domain.UserSiteConfig
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.DateTimeUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 * @author Mario Serrano Leones
 */
@Controller
@RequestMapping("/users")
class UsersController {

    @Autowired
    private CrudService crudService

    @Autowired
    private SiteService siteService

    @Autowired
    private UserService service

    @RequestMapping(value = ["/login", "/save"], method = RequestMethod.GET)
    ModelAndView login(HttpServletRequest request) {
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        def path = savedRequest?.requestURI
        ModelAndView mv = new ModelAndView(path == "/cms-admin" ? "users/loginAdmin" : "users/login")
        mv.addObject("title", "Inicio de Sesion")

        SiteActionManager.performAction("loginUser", mv, request)

        return mv
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.context.authentication
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth)
        }
        ModelAndView mv = new ModelAndView()
        mv.view = new RedirectView("/", false, true, false)
        return mv
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    ModelAndView saveUser(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("users/save")
        mv.addObject("title", "Registro de Usuarios")
        SiteActionManager.performAction("saveUser", mv, request, user)

        return mv
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
    ModelAndView resetPassword(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/resetpassword")
        mv.addObject("title", "Olvide mi Password")
        UserForm form = new UserForm()
        form.data = new User()
        mv.addObject("userForm", form)
        return mv
    }

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
    ModelAndView resetPassword(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("users/resetpassword")
        mv.addObject("title", "Olvide mi Password")
        SiteActionManager.performAction("resetPassword", mv, request, redirectAttributes, user)
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    ModelAndView account(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/account")
        mv.addObject("title", "Mi Cuenta")
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    ModelAndView changePassword(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/changepassword")
        mv.addObject("title", "Cambiar Password")
        SiteActionManager.performAction("updateUser", mv, request)
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    ModelAndView changePassword(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("users/accounts")
        SiteActionManager.performAction("changePassword", mv, request, redirectAttributes, user)
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    ModelAndView profile(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/profile")
        mv.addObject("title", "Mis Datos")

        SiteActionManager.performAction("updateUser", mv, request)

        mv.addObject("redirect", request.getParameter("redirect"))
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    ModelAndView profile(@Valid UserForm user, BindingResult bindingResult, HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("users/account")
        SiteActionManager.performAction("saveUserProfile", mv, request, redirectAttributes, user)

        if (request.getParameter("redirect") != null) {
            mv.view = new RedirectView(request.getParameter("redirect"), true, true, false)
        }

        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    ModelAndView addresses(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/addresses/table")

        SiteActionManager.performAction("showUserContactInfos", mv, request)
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/addresses", method = RequestMethod.POST)
    ModelAndView saveAddress(@Valid UserContactInfo userContactInfo, BindingResult bindingResult,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView()
        String redirect = request.getParameter("currentURI")
        if (request.getParameter("redirect") != null) {
            redirect = request.getParameter("redirect")
        }
        mv.addObject("redirect", redirect)

        SiteActionManager.performAction("saveUserContactInfo", mv, request, redirectAttributes, userContactInfo)

        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/addresses/add", method = RequestMethod.GET)
    ModelAndView addAddress(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/addresses/form")
        SiteActionManager.performAction("addUserContactInfo", mv, request)

        mv.addObject("redirect", request.getParameter("redirect"))

        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/addresses/{id}/edit", method = RequestMethod.GET)
    ModelAndView editAddress(@PathVariable Long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("users/addresses/form")
        SiteActionManager.performAction("editUserContactInfo", mv, request, id)
        mv.addObject("redirect", request.getParameter("redirect"))
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/addresses/{id}/remove", method = RequestMethod.GET)
    ModelAndView removeAddress(@PathVariable Long id, HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("users/addresses/table")
        mv.view = new RedirectView("/users/profile", true, true, false)
        SiteActionManager.performAction("removeUserContactInfo", mv, request, redirectAttributes, id)
        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/setCustomer", method = RequestMethod.POST)
    ModelAndView setCustomer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView()

        Site site = siteService.getSite(request)
        String id = request.getParameter("id")
        String code = request.getParameter("code")
        User customer = null

        if (code != null && !code.empty) {
            customer = crudService.findSingle(User.class,
                    QueryParameters.with("externalRef", QueryConditions.eq(code)).add("site", site))

            if (customer == null) {
                CMSUtil.addErrorMessage("No se encontro cliente con codigo: " + code, redirectAttributes)
            }
        } else if (id != null && !id.equals("0")) {
            try {
                customer = crudService.find(User.class, Long.parseLong(id))
            } catch (Exception e) {
            }
        }

        if (customer != null) {
            UserHolder.get().customer = customer
        }
        String redirect = request.getParameter("currentURI")
        if (request.getParameter("redirect") != null) {
            redirect = request.getParameter("redirect")
        }

        if (redirect == null) {
            redirect = "/"
        }

        mv.view = new RedirectView(redirect, true, true, false)

        return mv
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/mycustomers", method = RequestMethod.GET)
    ModelAndView myCustomers(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("users/mycustomers")
        SiteActionManager.performAction("showMyCustomers", mv, request, redirectAttributes)
        return mv
    }

    @RequestMapping(value = "/activate/{key}", method = RequestMethod.GET)
    ModelAndView activateUserByEmail(@PathVariable String key, HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {

        ModelAndView mv = new ModelAndView("users/validated")
        mv.addObject("valid", false)

        if (key == null || key.empty) {
            CMSUtil.addErrorMessage("Invalid validation key", redirectAttributes)
        } else {
            String message = ""

            Site site = siteService.getSite(request)

            if (site != null) {
                User user = service.getUserByValidationKey(site, key)

                if (user != null) {

                    UserSiteConfig config = service.getSiteConfig(site)

                    if (user.enabled) {
                        message = "El usuario " + user + " ya fue activado"

                    } else {
                        user.validationDate = DateTimeUtils.now()
                        user.validated = true

                        if (!config.registrationValidated) {
                            service.enableUser(user)
                            mv.addObject("valid", true)
                        }
                        mv.addObject("user", user)
                    }
                } else {
                    message = "Link invalido, no se encuentra usuario asociado"

                }
            } else {
                message = "Sitio web invalido"
            }
            mv.addObject("message", message)

        }

        return mv

    }

}
