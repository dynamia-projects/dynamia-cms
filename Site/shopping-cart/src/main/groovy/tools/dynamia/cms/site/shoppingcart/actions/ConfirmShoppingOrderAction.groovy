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
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.payment.PaymentException
import tools.dynamia.cms.site.payment.PaymentForm
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.PaymentHolder
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.site.users.api.UserProfile
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.domain.UserContactInfo
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
		ModelAndView mv = evt.getModelAndView()
        mv.setViewName("shoppingcart/confirm")

        mv.addObject("title", "Resumen de Pedido")

        ShoppingSiteConfig config = service.getConfiguration(evt.getSite())
        ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder()

        if (order.getId() != null) {
			order = crudService.find(ShoppingOrder.class, order.getId())
            ShoppingCartHolder.get().setCurrentOrder(order)
        }
		order.sync()
        order.setUserComments(evt.getRequest().getParameter("userComments"))
        order.setBillingAddress(loadContactInfo("billingAddress", evt))
        order.setShippingAddress(loadContactInfo("shippingAddress", evt))

        String customer = evt.getRequest().getParameter("customer")
        if (customer != null && !customer.equals("0")) {
			Long customerId = Long.parseLong(customer)
            User userCustomer = crudService.find(User.class, customerId)
            order.getShoppingCart().setCustomer(userCustomer)
        }

		String deliveryType = evt.getRequest().getParameter("deliveryType")
        if (deliveryType == null) {
			deliveryType = ""
        }
		if (deliveryType.equals("pickupAtStore")) {
			order.setPickupAtStore(true)
            order.setPayAtDelivery(false)
        } else if (deliveryType.equals("payAtDelivery")) {
			order.setPickupAtStore(false)
            order.setPayAtDelivery(true)
        }

		String paymentType = evt.getRequest().getParameter("paymentType")

        if (paymentType == null) {
			paymentType = ""
        }

		if (paymentType.equals("later")) {
			order.setPayLater(true)
        }

		try {

			validate(order, config)
            String name = order.getShoppingCart().getName()
            service.saveOrder(order)
            ShoppingCartHolder.get().setCurrentOrder(order)

            ShoppingCartHolder.get().removeCart(name)

            PaymentForm form = new PaymentForm()
            if (!order.isPayLater()) {
				try {
					PaymentGateway gateway = paymentService.findGateway(order.getTransaction().getGatewayId())
                    form = gateway.createForm(order.getTransaction())
                    PaymentHolder.get().setCurrentPaymentForm(form)
                } catch (PaymentException e) {
					System.err.println("GATEWAY EXCEPTION:: " + e.getMessage())
                    e.printStackTrace()
                }

			}

			mv.addObject("paymentForm", form)
            mv.addObject("shoppingOrder", order)

        } catch (ValidationError e) {
			SiteActionManager.performAction("checkoutShoppingCart", mv, evt.getRequest(), evt.getRedirectAttributes())
            CMSUtil.addErrorMessage(e.getMessage(), mv)
        }
	}

	private void validate(ShoppingOrder order, ShoppingSiteConfig config) {
		if (order.getBillingAddress() == null && config.isBillingAddressRequired()) {
			throw new ValidationError("Seleccione direccion de facturacion")
        }

		if (!order.isPickupAtStore() && order.getShippingAddress() == null && config.isShippingAddressRequired()) {
			throw new ValidationError("Seleccione direccion de envio o marque la opcion recoger en tienda")
        } else if (order.isPickupAtStore()) {
			order.setShippingAddress(null)
        }

		if (order.getShoppingCart() == null) {
			throw new ValidationError("La orden de pedido no tiene carrito de compra asociado")
        }

		if (order.getShoppingCart().getUser() == null) {
			throw new ValidationError("La orden de pedido no tiene usuario asociado")
        }

		if (order.getShoppingCart().getUser().getProfile() == UserProfile.SELLER
				&& order.getShoppingCart().getCustomer() == null) {
			throw new ValidationError("Seleccione cliente")
        }

	}

	private UserContactInfo loadContactInfo(String string, ActionEvent evt) {
		UserContactInfo userContactInfo = null

        try {
			Long id = Long.parseLong(evt.getRequest().getParameter(string))
            userContactInfo = crudService.find(UserContactInfo.class, id)
        } catch (Exception e) {
			// TODO: handle exception
		}

		return userContactInfo
    }

}