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
