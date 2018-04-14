package tools.dynamia.cms.admin.shopping.listeners;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.api.CMSListener;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import tools.dynamia.cms.site.payment.services.PaymentService;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;

@CMSListener
public class PaymentGatewayConfigListener extends CrudServiceListenerAdapter<PaymentGatewayConfig> {

	@Autowired
	private PaymentService service;

	@Override
	public void beforeCreate(PaymentGatewayConfig entity) {
		if (entity.getSource() == null || entity.getSource().isEmpty()) {
			String siteKey = SiteContext.get().getCurrent().getKey();
			entity.setSource(siteKey);
		}

		if (entity.getGatewayId() == null || entity.getGatewayId().isEmpty()) {
			PaymentGateway gateway = service.getDefaultGateway();
			if (gateway != null) {
				entity.setGatewayId(gateway.getId());
			}
		}
	}

	@Override
	public void beforeQuery(QueryParameters params) {
		if (params.getType() == PaymentGatewayConfig.class) {
			String siteKey = SiteContext.get().getCurrent().getKey();
			params.add("source", siteKey);
		}
	}

}
