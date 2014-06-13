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
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author mario_2
 */
@CMSAction
public class UpdateItemFromCartAction implements SiteAction {

    @Autowired
    private ShoppingCartService service;

    @Override
    public String getName() {
        return "updateItemFromCart";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        String code = (String) evt.getData();

        ShoppingCartItem item = ShoppingCartHolder.get().getCurrent().getItemByCode(code);
        if (item != null) {
            try {
                int quantity = Integer.parseInt(evt.getRequest().getParameter("quantity"));
                item.setQuantity(quantity);
                ShoppingCartHolder.get().getCurrent().compute();
                CMSUtil.addSuccessMessage(item.getName().toUpperCase() + " actualizado", evt.getRedirectAttributes());
            } catch (Exception e) {
                CMSUtil.addErrorMessage("Ingrese cantidad valida para " + item.getName().toUpperCase(), evt.getRedirectAttributes());
            }
        }
        mv.setView(new RedirectView("/shoppingcart", true, true, false));
        mv.addObject("title", "Carrito");

    }

}
