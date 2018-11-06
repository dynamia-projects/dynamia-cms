package tools.dynamia.cms.payment.admin.listeners

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
class PaymentGatewayAccountCrudListener extends CrudServiceListenerAdapter<PaymentGatewayAccount> {

    @Override
    void beforeCreate(PaymentGatewayAccount entity) {
        entity.source = SiteContext.get().current.key
    }

    @Override
    void beforeQuery(QueryParameters params) {

        if (params.getType() != null && BeanUtils.isAssignable(params.getType(), PaymentGatewayAccount.class)) {
            params.add("source", QueryConditions.eq(SiteContext.get().current.key))
        }

    }
}
