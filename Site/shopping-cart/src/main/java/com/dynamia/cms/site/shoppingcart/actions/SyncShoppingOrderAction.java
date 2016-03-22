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
package com.dynamia.cms.site.shoppingcart.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import com.dynamia.cms.site.shoppingcart.ShoppingCartUtils;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class SyncShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private PaymentService paymentService;

	@Override
	public String getName() {
		return "syncShoppingOrder";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		ShoppingOrder order = ShoppingCartHolder.get().getCurrentOrder();
		ShoppingCart cart = ShoppingCartUtils.getShoppingCart(evt.getModelAndView());

		if (order != null && cart != null && order.getShoppingCart().equals(cart)) {
			order.sync();
		}

	}

}
