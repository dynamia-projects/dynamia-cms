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

package tools.dynamia.cms.payment.admin

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction

@CMSModule
class PaymentAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "payments"
    }

    @Override
    String getName() {
        return "Payments"
    }

    @Override
    String getImage() {
        return "dollar"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("paymentsGateways", "Gateways Accounts", PaymentGatewayAccount.class, false,
                        true),
                new AdminModuleOption("payments", "Transactions", PaymentTransaction.class),
                new AdminModuleOption("manualPayments", "Manual Payments", ManualPayment.class)

        ]
    }
}
