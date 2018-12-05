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
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.core.html.Option
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.User

@CMSAction
class NewPaymentAction implements SiteAction {

	@Autowired
	private tools.dynamia.cms.shoppingcart.services.ShoppingCartService service

    @Override
    String getName() {
		return "createNewPayment"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.modelAndView
        mv.viewName = "payment/new"
        mv.addObject("title", "Nuevo Pago Manual")
        tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig config = service.getConfiguration(evt.site)

        ManualPayment pay = new ManualPayment()
        pay.source = evt.site.key
        pay.registrator = UserHolder.get().fullName
        pay.registratorId = UserHolder.get().current.id.toString()
        pay.registratorCode = UserHolder.get().current.code

        User customer = UserHolder.get().customer
        if (customer == null) {
			CMSUtil.addErrorMessage("Seleccione cliente para registrar pago", mv)
            return
        }
        pay.payer = customer.fullName
        pay.payerCode = customer.code
        pay.payerId = customer.id.toString()

        loadPaymentTypes(mv, config)
        mv.addObject("payment", pay)

        PaymentHolder.get().currentManualPayment = pay

    }

	private void loadPaymentTypes(ModelAndView mv, tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig config) {
		String types = config.paymentTypes
        if (types != null) {
			mv.addObject("paymentTypes", Option.buildFromArray(types.split(","), null))
        }
	}

}
