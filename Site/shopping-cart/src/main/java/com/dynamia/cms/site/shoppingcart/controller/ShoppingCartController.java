/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.actions.SiteActionManager;

/**
 *
 * @author mario_2
 */
@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

	@RequestMapping(value = { "/{name}" }, method = RequestMethod.GET)
	public ModelAndView main(@PathVariable String name, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("viewShoppingCart", mv, request);

		return mv;
	}

	@RequestMapping(value = "/{name}/print", method = RequestMethod.GET)
	public ModelAndView print(@PathVariable String name, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		mv.addObject("title", "Listado ");
		SiteActionManager.performAction("printShoppingCart", mv, request);

		return mv;
	}

	@RequestMapping(value = "/{name}/add/{itemCode}", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView add(@PathVariable String name, @PathVariable String itemCode, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		String redirect = request.getParameter("currentURI");
		mv.setView(new RedirectView(redirect, true, true, false));
		SiteActionManager.performAction("addItemToCart", mv, request, redirectAttributes, itemCode);
		SiteActionManager.performAction("syncShoppingOrder", mv, request, redirectAttributes, itemCode);

		return mv;
	}

	@RequestMapping(value = "/{name}/remove/{itemCode}", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView remove(@PathVariable String name, @PathVariable String itemCode, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		String redirect = request.getParameter("currentURI");
		mv.setView(new RedirectView(redirect, true, true, false));
		SiteActionManager.performAction("removeItemFromCart", mv, request, redirectAttributes, itemCode);
		SiteActionManager.performAction("syncShoppingOrder", mv, request, redirectAttributes, itemCode);

		return mv;
	}

	@RequestMapping(value = "/{name}/clear", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView clear(@PathVariable String name, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		String redirect = request.getParameter("currentURI");
		mv.setView(new RedirectView(redirect, true, true, false));
		SiteActionManager.performAction("clearShoppingCart", mv, request, redirectAttributes, null);

		return mv;
	}

	@RequestMapping(value = "/{name}/update/{itemCode}", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView update(@PathVariable String name, @PathVariable String itemCode, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("/shopping");
		mv.addObject("cartName", name);
		SiteActionManager.performAction("updateItemFromCart", mv, request, redirectAttributes, itemCode);

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/{name}/checkout", method = { RequestMethod.GET })
	public ModelAndView checkout(@PathVariable String name, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("checkoutShoppingCart", mv, request, redirectAttributes);

		return mv;
	}

	@RequestMapping(value = "/{name}/confirm", method = { RequestMethod.GET })
	public ModelAndView confirm(@PathVariable String name) {

		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView("/", true, true, false));
		return mv;
	}

	@RequestMapping(value = "/{name}/cancel", method = { RequestMethod.GET })
	public ModelAndView cancel(@PathVariable String name, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("cancelShoppingOrder", mv, request, redirectAttributes);
		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/{name}/confirm", method = { RequestMethod.POST })
	public ModelAndView confirm(@PathVariable String name, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("confirmShoppingOrder", mv, request, redirectAttributes);

		return mv;
	}

}
