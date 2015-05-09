package com.dynamia.cms.admin.shopping.listeners;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.api.CMSListener;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;

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
