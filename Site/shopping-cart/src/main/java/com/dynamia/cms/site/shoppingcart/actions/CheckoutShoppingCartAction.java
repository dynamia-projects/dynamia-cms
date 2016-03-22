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
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.services.UserService;

import tools.dynamia.domain.ValidationError;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class CheckoutShoppingCartAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private UserService userService;

	@Override
	public String getName() {
		return "checkoutShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv);
		ShoppingSiteConfig config = service.getConfiguration(evt.getSite());

		if (shoppingCart == null || shoppingCart.getQuantity() == 0) {
			CMSUtil.addWarningMessage("El carrito de compra esta vacio", mv);
			mv.setView(new RedirectView("/", false, true, false));
		} else if (config.isPaymentEnabled() || UserHolder.get().isAdmin()) {
			mv.setViewName("shoppingcart/checkout");

			mv.addObject("title", "Confirmar Pedido");
			mv.addObject("userContactInfos", userService.getContactInfos(UserHolder.get().getCurrent()));

			try {
				ShoppingOrder order = service.createOrder(shoppingCart, config);
				order.getTransaction().setClientIP(evt.getRequest().getRemoteAddr());
				ShoppingCartHolder.get().setCurrentOrder(order);

				mv.addObject("shoppingOrder", order);
			} catch (ValidationError e) {
				CMSUtil.addWarningMessage(e.getMessage(), mv);
				SiteActionManager.performAction("viewShoppingCart", mv, evt.getRequest());
			}
		} else {
			mv.setView(new RedirectView("/", false, true, false));
			CMSUtil.addErrorMessage("Sistema de pagos dehabilitado temporalmente", mv);
		}

	}

}
