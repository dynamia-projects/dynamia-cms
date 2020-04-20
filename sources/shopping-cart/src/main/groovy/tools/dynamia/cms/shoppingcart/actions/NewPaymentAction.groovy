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
