/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;

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
        evt.getModelAndView().addObject("title", "Carrito");
        evt.getModelAndView().setViewName("shoppingcart/details");

    }

}
