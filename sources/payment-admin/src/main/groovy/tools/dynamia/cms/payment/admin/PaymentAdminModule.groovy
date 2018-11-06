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
