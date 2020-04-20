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
