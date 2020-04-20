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
package tools.dynamia.cms.mail.controllers

import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.mail.domain.MailingContact

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
@RequestMapping(value = "/mailing", method = [RequestMethod.GET, RequestMethod.POST])
class MailingController {

    @RequestMapping("/subscribe")
    ModelAndView subscribe(@Valid MailingContact contact, BindingResult result, HttpServletRequest request,
                           final RedirectAttributes redirectAttributes) {
        if (result.hasFieldErrors("emailAddress")) {

        }

        ModelAndView mv = new ModelAndView()
        String redirect = request.getParameter("currentURI")
        mv.view = new RedirectView(redirect, true, true, false)
        SiteActionManager.performAction("addMailingContact", mv, request, redirectAttributes, contact)

        return mv
    }

}
