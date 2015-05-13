/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ViewShoppingCartAction implements SiteAction {

	

	@Override
	public String getName() {
		return "viewShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/details");

		ShoppingCartUtils.loadConfig(evt.getSite(), mv);

	}

}
