/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

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

        ShoppingCartHolder.get().getCurrent().getItems().clear();
        ShoppingCartHolder.get().getCurrent().compute();

        CMSUtil.addSuccessMessage("Carrito limpiado exitosamente", evt.getRedirectAttributes());

    }

}