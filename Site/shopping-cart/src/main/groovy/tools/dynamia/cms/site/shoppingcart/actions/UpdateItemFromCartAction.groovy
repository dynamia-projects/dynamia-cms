/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

import tools.dynamia.commons.StringUtils;

/**
 *
 * @author Mario Serrano Leones
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
        ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv);
        if (shoppingCart != null) {
            ShoppingCartItem item = shoppingCart.getItemByCode(code);
            if (item != null && item.isEditable()) {
                try {
                    int quantity = Integer.parseInt(evt.getRequest().getParameter("quantity"));
                    item.setQuantity(quantity);
                    updateChildren(item);
                    shoppingCart.compute();
                    CMSUtil.addSuccessMessage(item.getName().toUpperCase() + " actualizado", evt.getRedirectAttributes());
                } catch (Exception e) {
                    CMSUtil.addErrorMessage("Ingrese cantidad valida para " + item.getName().toUpperCase(), evt.getRedirectAttributes());
                }
            }
            mv.setView(new RedirectView("/shoppingcart/" + shoppingCart.getName(), true, true, false));
            mv.addObject("title", StringUtils.capitalizeAllWords(shoppingCart.getName()));
        }
    }

    private void updateChildren(ShoppingCartItem item) {
        if (item.getChildren() != null && !item.getChildren().isEmpty()) {
            item.getChildren().forEach { c -> c.setQuantity(item.getQuantity()) };
        }
    }

}
