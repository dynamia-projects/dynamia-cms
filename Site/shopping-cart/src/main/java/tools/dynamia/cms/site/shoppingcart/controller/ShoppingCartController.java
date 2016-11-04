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
package tools.dynamia.cms.site.shoppingcart.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import tools.dynamia.cms.site.core.actions.SiteActionManager;

/**
 *
 * @author Mario Serrano Leones
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
	public ModelAndView clear(@PathVariable String name, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
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

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/{name}/checkout", method = { RequestMethod.GET })
	public ModelAndView checkout(@PathVariable String name, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
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
	public ModelAndView cancel(@PathVariable String name, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("cancelShoppingOrder", mv, request, redirectAttributes);
		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/{name}/confirm", method = { RequestMethod.POST })
	public ModelAndView confirm(@PathVariable String name, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("confirmShoppingOrder", mv, request, redirectAttributes);

		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/{name}/complete", method = { RequestMethod.GET })
	public ModelAndView complete(@PathVariable String name, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("cartName", name);
		SiteActionManager.performAction("completeShoppingOrder", mv, request, redirectAttributes);

		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/orders", method = { RequestMethod.GET })
	public ModelAndView orders(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();

		SiteActionManager.performAction("showMyShoppingOrders", mv, request, redirectAttributes);

		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/order/{id}", method = { RequestMethod.GET })
	public ModelAndView viewOrder(@PathVariable Long id, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();
		SiteActionManager.performAction("viewShoppingOrder", mv, request, redirectAttributes, id);

		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/orders/status", method = { RequestMethod.GET })
	public ModelAndView ordersStatus(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();

		SiteActionManager.performAction("showMyOrdersStatus", mv, request, redirectAttributes, null);

		return mv;
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/orders/status/{customer}", method = { RequestMethod.GET })
	public ModelAndView ordersStatusCustomer(@PathVariable String customer, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView();

		SiteActionManager.performAction("showMyOrdersStatus", mv, request, redirectAttributes, customer);

		return mv;
	}

}
