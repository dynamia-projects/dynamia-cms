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
