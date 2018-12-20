/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.cms.shopping.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SendManualPaymentAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service

    @Autowired
	private ShoppingCartService shoppingCartService

    SendManualPaymentAction() {
        name = "Send Payments"
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Payment.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Site site = SiteContext.get().current
        ShoppingSiteConfig cfg = shoppingCartService.getConfiguration(site)

        if (cfg.paymentsSenderURL != null && !cfg.paymentsSenderURL.empty) {
			service.sendManualPayments(site.key, cfg.paymentsSenderURL, cfg.parametersAsMap)
            service.sendPayments(site.key, cfg.paymentsSenderURL, cfg.parametersAsMap)
            UIMessages.showMessage("Sending... OK")
        } else {
			UIMessages.showMessage("Payments URL is not configured", MessageType.ERROR)
        }
        evt.controller.doQuery()
    }

}
