package com.dynamia.cms.site.mail.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.mail.domain.MailingContact;
import com.dynamia.tools.integration.sterotypes.Controller;

@Controller
@RequestMapping(value = "/mailing", method = { RequestMethod.GET, RequestMethod.POST })
public class MailingController {

	@RequestMapping("/subscribe")
	public ModelAndView subscribe(@Valid MailingContact contact, BindingResult result, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		if (result.hasFieldErrors("emailAddress")) {

		}

		ModelAndView mv = new ModelAndView();
		String redirect = request.getParameter("currentURI");
		mv.setView(new RedirectView(redirect, true, true, false));		
		SiteActionManager.performAction("addMailingContact", mv, request, redirectAttributes, contact);

		return mv;
	}

}
