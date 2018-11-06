package tools.dynamia.cms.payment.admin.listeners

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
class PaymentCrudListener extends CrudServiceListenerAdapter<Payment> {

	@Override
    void beforeCreate(Payment entity) {
		if (entity.source == null) {
			try {
                entity.source = SiteContext.get().current.key
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace()
            }
		}

	}

	@Override
    void beforeQuery(QueryParameters params) {
		Class entityClass = params.getType()

        if (BeanUtils.isAssignable(entityClass, Payment.class)) {

			if (!params.containsKey("source")) {
				String source = null
                try {
					source = SiteContext.get().current.key
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace()
                }

				if (source != null) {
					params.put("source", QueryConditions.eq(source))
                }
			}
		}
	}
}
