/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.services.UserService;
import com.dynamia.tools.domain.ValidationError;

/**
 *
 * @author mario_2
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
			CMSUtil.addWarningMessage("El carrito de compra esta vacio", evt.getRedirectAttributes());
			mv.setView(new RedirectView("/", false, true, false));
		} else if (config.isPaymentEnabled() || UserHolder.get().isSuperadmin()) {
			mv.setViewName("shoppingcart/checkout");

			ShoppingCartUtils.loadConfig(evt.getSite(), mv);

			mv.addObject("title", "Confirmar Pedido");
			mv.addObject("userContactInfos", userService.getContactInfos(UserHolder.get().getCurrent()));

			try {
				ShoppingOrder order = service.createOrder(shoppingCart, config);
				order.getTransaction().setClientIP(evt.getRequest().getRemoteAddr());
				ShoppingCartHolder.get().setCurrentOrder(order);

				mv.addObject("shoppingOrder", order);
			} catch (ValidationError e) {
				CMSUtil.addWarningMessage(e.getMessage(), evt.getRedirectAttributes());
				mv.setView(new RedirectView("/", true));
			}
		} else {
			mv.setViewName("redirect:/");
			CMSUtil.addErrorMessage("Sistema de pagos dehabilitado temporalmente", evt.getRedirectAttributes());
		}

	}

}
