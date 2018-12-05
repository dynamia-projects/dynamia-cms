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
