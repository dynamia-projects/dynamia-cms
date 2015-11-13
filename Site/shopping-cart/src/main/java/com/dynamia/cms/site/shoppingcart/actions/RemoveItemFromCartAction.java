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
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class RemoveItemFromCartAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Override
	public String getName() {
		return "removeItemFromCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		String code = (String) evt.getData();
		ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv);
		if (shoppingCart != null) {
			boolean r = shoppingCart.removeItem(code);
			if (r) {
				CMSUtil.addSuccessMessage("Item quitado exitosamente del carrito", evt.getRedirectAttributes());
			}
		}

	}

}
