/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.domain.enums.ShoppingCartStatus
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class CompleteShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "completeShoppingOrder"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView
        mv.viewName = "shoppingcart/complete"

        mv.addObject("title", "Pedido Confirmado Exitosamente!")

        ShoppingSiteConfig config = service.getConfiguration(evt.site)
        ShoppingOrder order = ShoppingCartHolder.get().currentOrder

        try {

			validate(order, config)
            String name = order.shoppingCart.name
            if (order.transaction.status != PaymentTransactionStatus.COMPLETED) {
                order.shoppingCart.status = ShoppingCartStatus.COMPLETED
                order.transaction.status = PaymentTransactionStatus.COMPLETED
                service.saveOrder(order)
                service.notifyOrderCompleted(order)
            }

			mv.addObject("shoppingOrder", order)

            UserHolder.get().customer = null

        } catch (ValidationError e) {
			SiteActionManager.performAction("confirmShoppingCart", mv, evt.request, evt.redirectAttributes)
            CMSUtil.addErrorMessage(e.message, mv)
        } catch (Exception e) {
			e.printStackTrace()
        }
	}

	private void validate(ShoppingOrder order, ShoppingSiteConfig config) {
		if (!order.payLater || !config.allowEmptyPayment) {
			throw new ValidationError("El sitio web no permite este tipo de pedidos")
        }

	}

	private UserContactInfo loadContactInfo(String string, ActionEvent evt) {
		UserContactInfo userContactInfo = null

        try {
			Long id = Long.parseLong(evt.request.getParameter(string))
            userContactInfo = crudService.find(UserContactInfo.class, id)
        } catch (Exception e) {
			// TODO: handle exception
		}

		return userContactInfo
    }

}
