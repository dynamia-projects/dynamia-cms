package tools.dynamia.cms.payment.admin.controllers

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.zk.crud.CrudController

class PaymentGatewayAccountCrudController extends CrudController<PaymentGatewayAccount> {

    @Override
    protected void afterCreate() {
        entity.source = SiteContext.get().current.key
    }


}
