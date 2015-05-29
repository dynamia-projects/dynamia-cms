/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.payment.PaymentForm;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class ConfirmShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "confirmShoppingOrder";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/confirm");

		mv.addObject("title", "Resumen de Pedido");
		ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder();
		if (order.getId() != null) {
			order = crudService.find(ShoppingOrder.class, order.getId());
			ShoppingCartHolder.get().setCurrentOrder(order);
		}
		order.sync();
		order.setUserComments(evt.getRequest().getParameter("userComments"));
		order.setBillingAddress(loadContactInfo("billingAddress", evt));
		order.setShippingAddress(loadContactInfo("shippingAddress", evt));

		try {

			validate(order);
			String name = order.getShoppingCart().getName();
			service.saveOrder(order);
			ShoppingCartHolder.get().setCurrentOrder(order);
			ShoppingCartHolder.get().removeCart(name);

			PaymentGateway gateway = paymentService.findGateway(order.getTransaction().getGatewayId());
			PaymentForm form = gateway.createForm(order.getTransaction());

			mv.addObject("shoppingOrder", order);
			mv.addObject("paymentForm", form);

		} catch (ValidationError e) {
			mv.setViewName("redirect:/");
			CMSUtil.addErrorMessage(e.getMessage(), evt.getRedirectAttributes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validate(ShoppingOrder order) {
		if (order.getBillingAddress() == null) {
			throw new ValidationError("Seleccione direccion de facturacion");
		}

		if (order.getShippingAddress() == null) {
			throw new ValidationError("Seleccione direccion de envio");
		}

		if (order.getShoppingCart() == null) {
			throw new ValidationError("La orden de pedido no tiene carrito de compra asociado");
		}

		if (order.getShoppingCart().getUser() == null) {
			throw new ValidationError("La orden de pedido no tiene usuario asociado");
		}

	}

	private UserContactInfo loadContactInfo(String string, ActionEvent evt) {
		UserContactInfo userContactInfo = null;

		try {
			Long id = Long.parseLong(evt.getRequest().getParameter(string));
			userContactInfo = crudService.find(UserContactInfo.class, id);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return userContactInfo;
	}

}
