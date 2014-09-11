/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;

/**
 *
 * @author mario_2
 */
@CMSAction
public class PrintShoppingCartAction implements SiteAction {

	@Override
	public String getName() {
		return "printShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ShoppingCartUtils.getShoppingCart(evt.getModelAndView());
		evt.getModelAndView().setViewName("shoppingcart/print");

	}

}
