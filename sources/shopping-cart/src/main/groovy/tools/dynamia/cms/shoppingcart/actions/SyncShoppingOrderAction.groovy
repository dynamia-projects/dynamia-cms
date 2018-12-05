/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.ShoppingCartUtils
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class SyncShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Override
    String getName() {
		return "syncShoppingOrder"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ShoppingOrder order = ShoppingCartHolder.get().currentOrder
        SiteAware cart = ShoppingCartUtils.getShoppingCart(evt.modelAndView)

        if (order != null && cart != null && order.shoppingCart.equals(cart)) {
			order.sync()
        }

	}

}
