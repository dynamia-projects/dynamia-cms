/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class CancelShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "cancelShoppingOrder";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setView(new RedirectView("/", true, true, false));

		ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder();
		if (order == null) {
			return;
		}

		if (order.getId() != null) {
			order = crudService.find(ShoppingOrder.class, order.getId());
			ShoppingCartHolder.get().setCurrentOrder(order);
		}
		order.sync();

		try {
			service.cancelOrder(order);
			CMSUtil.addSuccessMessage("Pedido No." + order.getNumber() + " cancelado exitosamente", evt.getRedirectAttributes());
		} catch (ValidationError e) {			
			CMSUtil.addErrorMessage(e.getMessage(), evt.getRedirectAttributes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
