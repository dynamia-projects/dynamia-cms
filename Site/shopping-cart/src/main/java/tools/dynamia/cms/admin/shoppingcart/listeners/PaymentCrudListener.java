package tools.dynamia.cms.admin.shoppingcart.listeners;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.payment.api.Payment;
import tools.dynamia.commons.BeanUtils;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Listener;

@Listener
public class PaymentCrudListener extends CrudServiceListenerAdapter<Payment> {

	@Override
	public void beforeCreate(Payment entity) {
		if (entity.getSource() == null) {
			try {
				entity.setSource(SiteContext.get().getCurrent().getKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void beforeQuery(QueryParameters params) {
		Class entityClass = params.getType();

		if (BeanUtils.isAssignable(entityClass, Payment.class)) {

			if (!params.containsKey("source")) {
				String source = null;
				try {
					source = SiteContext.get().getCurrent().getKey();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (source != null) {
					params.put("source", QueryConditions.eq(source));
				}
			}
		}
	}
}
