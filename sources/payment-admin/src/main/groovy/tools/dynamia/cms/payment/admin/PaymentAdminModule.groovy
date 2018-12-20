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
