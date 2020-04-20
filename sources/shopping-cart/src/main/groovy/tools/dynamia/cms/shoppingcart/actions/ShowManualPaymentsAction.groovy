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
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.BigDecimalUtils

@CMSAction
class ShowManualPaymentsAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Autowired
	private UserService userService

    @Override
    String getName() {
		return "showManualPayments"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.modelAndView
        mv.viewName = "payment/manuallist"

        mv.addObject("title", "Pagos Registrados")

        ShoppingSiteConfig cfg = service.getConfiguration(evt.site)
        String customer = (String) evt.data

        if (customer == null) {
			if (UserHolder.get().customer != null) {
				customer = UserHolder.get().customer.externalRef
            } else if (UserHolder.get().current.profile == UserProfile.USER) {
				customer = UserHolder.get().current.externalRef
            }
		}

		User user = userService.getByExternalRef(evt.site, customer)
        if (user != null) {
			mv.addObject("title", "Pagos Registrados - " + user.fullName)
            List<ManualPayment> payments = paymentService.findManualPaymentsByPayerId(evt.site.key,
					user.id.toString())
            BigDecimal total = BigDecimalUtils.sum("amount", payments)

            mv.addObject("payments", payments)
            mv.addObject("total", total)
            mv.addObject("customer", user)

            if (payments.empty) {
				CMSUtil.addSuccessMessage("No se encontraron pagos manuales registrados", mv)
            }
		}

	}

}
