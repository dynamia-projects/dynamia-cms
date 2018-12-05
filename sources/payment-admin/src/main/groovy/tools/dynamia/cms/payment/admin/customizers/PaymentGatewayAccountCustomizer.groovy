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

package tools.dynamia.cms.payment.admin.customizers

import org.zkoss.zk.ui.event.Events
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.integration.Containers
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.ViewCustomizer
import tools.dynamia.zk.ui.ProviderPickerBox
import tools.dynamia.zk.viewers.form.FormView

class PaymentGatewayAccountCustomizer implements ViewCustomizer<FormView<PaymentGatewayAccount>> {


    @Override
    void customize(FormView<PaymentGatewayAccount> view) {
        def gatewayPicker = view.getFieldComponent("gatewayId").inputComponent as ProviderPickerBox

        gatewayPicker.addEventListener(Events.ON_SELECT, {
            def account = view.value
            if (account.gatewayId != null) {
                if (!account.configuration.empty) {
                    UIMessages.showQuestion("Do you want reset configuration?", { resetConfig(view, account) })
                } else {
                    resetConfig(view, account)
                }
            }
        })

        view.addEventListener(FormView.ON_VALUE_CHANGED, {
            gatewayPicker.disabled = view.value.id != null
        })
    }

    def resetConfig(FormView<PaymentGatewayAccount> view, PaymentGatewayAccount account) {
        account.configuration.clear()

        def service = Containers.get().findObject(PaymentService)
        def gateway = service.findGateway(account.gatewayId)
        if (gateway != null) {
            gateway.requiredParams.each {
                account.addConfig(new PaymentGatewayConfig(gateway, it, "", account.source))
            }
            view.updateUI()
        }
    }
}
