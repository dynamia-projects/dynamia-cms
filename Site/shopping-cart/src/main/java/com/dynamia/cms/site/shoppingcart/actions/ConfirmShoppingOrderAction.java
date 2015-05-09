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
import com.dynamia.cms.site.payment.PaymentForm;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ConfirmShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private PaymentService paymentService;

	@Override
	public String getName() {
		return "confirmShoppingOrder";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/confirm");

		ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder();
		PaymentGateway gateway = paymentService.findGateway(order.getTransaction().getGatewayId());
		PaymentForm form = gateway.createForm(order.getTransaction());

		mv.addObject("shoppingOrder", order);
		mv.addObject("paymentForm", form);

	}

}
