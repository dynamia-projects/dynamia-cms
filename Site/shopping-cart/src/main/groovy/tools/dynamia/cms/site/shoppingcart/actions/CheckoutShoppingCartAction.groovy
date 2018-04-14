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
package tools.dynamia.cms.site.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.site.shoppingcart.ShoppingCartUtils
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.services.UserService
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
