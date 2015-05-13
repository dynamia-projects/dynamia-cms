/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ClearShoppingCartAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Override
	public String getName() {
		return "clearShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv);
		if (shoppingCart != null) {
			shoppingCart.getItems().clear();
			shoppingCart.compute();

			if (shoppingCart.getName().equals("shop")) {
				ShoppingCartHolder.get().setCurrentOrder(null);
			}

			CMSUtil.addSuccessMessage("Carrito limpiado exitosamente", evt.getRedirectAttributes());
		}

	}

}
