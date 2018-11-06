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
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.payment.PaymentException
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ConfirmShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "confirmShoppingOrder"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView
        mv.viewName = "shoppingcart/confirm"

        mv.addObject("title", "Resumen de Pedido")

        ShoppingSiteConfig config = service.getConfiguration(evt.site)
        ShoppingOrder order = ShoppingCartHolder.get().currentOrder

        if (order.id != null) {
			order = crudService.find(ShoppingOrder.class, order.id)
            ShoppingCartHolder.get().currentOrder = order
        }
		order.sync()
        order.userComments = evt.request.getParameter("userComments")
        order.billingAddress = loadContactInfo("billingAddress", evt)
        order.shippingAddress = loadContactInfo("shippingAddress", evt)

        String customer = evt.request.getParameter("customer")
        if (customer != null && !customer.equals("0")) {
			Long customerId = Long.parseLong(customer)
            User userCustomer = crudService.find(User.class, customerId)
            order.shoppingCart.customer = userCustomer
        }

		String deliveryType = evt.request.getParameter("deliveryType")
        if (deliveryType == null) {
			deliveryType = ""
        }
		if (deliveryType.equals("pickupAtStore")) {
            order.pickupAtStore = true
            order.payAtDelivery = false
        } else if (deliveryType.equals("payAtDelivery")) {
            order.pickupAtStore = false
            order.payAtDelivery = true
        }

		String paymentType = evt.request.getParameter("paymentType")

        if (paymentType == null) {
			paymentType = ""
        }

		if (paymentType == "later") {
            order.payLater = true
        }

		try {

			validate(order, config)
            String name = order.shoppingCart.name
            service.saveOrder(order)
            ShoppingCartHolder.get().currentOrder = order

            ShoppingCartHolder.get().removeCart(name)

            PaymentForm form = new PaymentForm()
            if (!order.payLater) {
				try {
					PaymentGateway gateway = paymentService.findGateway(order.transaction.gatewayId)

                    form = gateway.createForm(order.transaction)
                    PaymentHolder.get().currentPaymentForm = form
                } catch (PaymentException e) {
					System.err.println("GATEWAY EXCEPTION:: " + e.message)
                    e.printStackTrace()
                }

			}

			mv.addObject("paymentForm", form)
            mv.addObject("shoppingOrder", order)

        } catch (ValidationError e) {
			SiteActionManager.performAction("checkoutShoppingCart", mv, evt.request, evt.redirectAttributes)
            CMSUtil.addErrorMessage(e.message, mv)
        }
	}

	private void validate(ShoppingOrder order, ShoppingSiteConfig config) {
		if (order.billingAddress == null && config.billingAddressRequired) {
			throw new ValidationError("Seleccione direccion de facturacion")
        }

		if (!order.pickupAtStore && order.shippingAddress == null && config.shippingAddressRequired) {
			throw new ValidationError("Seleccione direccion de envio o marque la opcion recoger en tienda")
        } else if (order.pickupAtStore) {
            order.shippingAddress = null
        }

		if (order.shoppingCart == null) {
			throw new ValidationError("La orden de pedido no tiene carrito de compra asociado")
        }

		if (order.shoppingCart.user == null) {
			throw new ValidationError("La orden de pedido no tiene usuario asociado")
        }

		if (order.shoppingCart.user.profile == UserProfile.SELLER
				&& order.shoppingCart.customer == null) {
			throw new ValidationError("Seleccione cliente")
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
