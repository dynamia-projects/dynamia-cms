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
package tools.dynamia.cms.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.shoppingcart.ShoppingCartUtils
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.StringUtils

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class UpdateItemFromCartAction implements SiteAction {

    @Autowired
    private ShoppingCartService service

    @Override
    String getName() {
        return "updateItemFromCart"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        String code = (String) evt.data
        ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv)
        if (shoppingCart != null) {
            tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem item = shoppingCart.getItemByCode(code)
            if (item != null && item.editable) {
                try {
                    int quantity = Integer.parseInt(evt.request.getParameter("quantity"))
                    item.quantity = quantity
                    updateChildren(item)
                    shoppingCart.compute()
                    CMSUtil.addSuccessMessage(item.name.toUpperCase() + " actualizado", evt.redirectAttributes)
                } catch (Exception e) {
                    CMSUtil.addErrorMessage("Ingrese cantidad valida para " + item.name.toUpperCase(), evt.redirectAttributes)
                }
            }
            mv.view = new RedirectView("/shoppingcart/" + shoppingCart.name, true, true, false)
            mv.addObject("title", StringUtils.capitalizeAllWords(shoppingCart.name))
        }
    }

    private void updateChildren(tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem item) {
        if (item.children != null && !item.children.empty) {
            item.children.forEach { c -> c.quantity = item.quantity }
        }
    }

}
