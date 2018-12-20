/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
