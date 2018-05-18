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
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.shoppingcart.ShoppingCartUtils
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class AddItemToCartAction implements SiteAction {

    @Autowired
    private ShoppingCartService service

    @Override
    String getName() {
        return "addItemToCart"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        String code = evt.data as String
        int qty = 1
        try {
            qty = Integer.parseInt(evt.request.getParameter("qty"))
        } catch (Exception e) {
            // TODO: handle exception
        }

        ShoppingCartItem item = service.getItem(evt.site, code)
        if (item != null) {
            ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv)
            if (shoppingCart != null) {
                shoppingCart.addItem(item, qty)
                if (item.children != null && !item.children.empty) {
                    item.children.forEach { c -> shoppingCart.addItem(c) }
                }
                CMSUtil.addSuccessMessage(item.name.toUpperCase() + " agregado exitosamente al carrito",
                        evt.redirectAttributes)
            }
        } else {
            CMSUtil.addSuccessMessage("No encontrado item con codigo $code",
                    evt.redirectAttributes)
        }

    }

}
