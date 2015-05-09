/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class CheckoutShoppingCartAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Override
	public String getName() {
		return "checkoutShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv);
		ShoppingSiteConfig config = service.getConfiguration(evt.getSite());
		if (config.isPaymentEnabled()) {
			mv.setViewName("shoppingcart/checkout");

			ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder();
			if (order == null) {
				order = service.createOrder(shoppingCart, config);
				ShoppingCartHolder.get().setCurrentOrder(order);
			}
			mv.addObject("shoppingOrder", order);
		} else {
			mv.setView(new RedirectView("/", true));
		}

	}

}
