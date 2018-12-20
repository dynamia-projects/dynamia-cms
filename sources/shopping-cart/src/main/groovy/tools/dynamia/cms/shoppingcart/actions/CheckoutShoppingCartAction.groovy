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
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.ShoppingCartUtils
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.domain.ValidationError

/**
 * @author Mario Serrano Leones
 */
@CMSAction
class CheckoutShoppingCartAction implements SiteAction {

    @Autowired
    private ShoppingCartService service

    @Autowired
    private UserService userService

    @Override
    String getName() {
        return "checkoutShoppingCart"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView

        ShoppingCart shoppingCart = ShoppingCartUtils.getShoppingCart(mv)
        ShoppingSiteConfig config = service.getConfiguration(evt.site)

        if (shoppingCart == null || shoppingCart.quantity == 0) {
            CMSUtil.addWarningMessage("El carrito de compra esta vacio", mv)
            mv.view = new RedirectView("/", false, true, false)
        } else if (config.paymentEnabled || UserHolder.get().admin) {
            mv.viewName = "shoppingcart/checkout"

            if (UserHolder.get().seller) {
                mv.viewName = "shoppingcart/checkoutSeller"
            }

            mv.addObject("title", "Confirmar Pedido")
            mv.addObject("userContactInfos", userService.getContactInfos(UserHolder.get().current))

            try {
                ShoppingOrder order = service.createOrder(shoppingCart, config)
                order.transaction.clientIP = evt.request.remoteAddr
                ShoppingCartHolder.get().currentOrder = order

                mv.addObject("shoppingOrder", order)
            } catch (ValidationError e) {
                CMSUtil.addErrorMessage(e.message, mv)
                SiteActionManager.performAction("viewShoppingCart", mv, evt.request)
            }
        } else {
            mv.view = new RedirectView("/", false, true, false)
            CMSUtil.addErrorMessage("Sistema de pagos dehabilitado temporalmente", mv)
        }

    }

}
