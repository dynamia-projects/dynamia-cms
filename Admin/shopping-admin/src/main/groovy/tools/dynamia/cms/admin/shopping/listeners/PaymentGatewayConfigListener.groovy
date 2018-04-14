package tools.dynamia.cms.admin.shopping.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.api.CMSListener
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter

@CMSListener
class PaymentGatewayConfigListener extends CrudServiceListenerAdapter<PaymentGatewayConfig> {

	@Autowired
	private PaymentService service

    @Override
    void beforeCreate(PaymentGatewayConfig entity) {
		if (entity.source == null || entity.source.empty) {
			String siteKey = SiteContext.get().current.key
            entity.source = siteKey
        }

		if (entity.gatewayId == null || entity.gatewayId.empty) {
			PaymentGateway gateway = service.defaultGateway
            if (gateway != null) {
                entity.gatewayId = gateway.id
            }
		}
	}

	@Override
    void beforeQuery(QueryParameters params) {
		if (params.getType() == PaymentGatewayConfig.class) {
			String siteKey = SiteContext.get().current.key
            params.add("source", siteKey)
        }
	}

}
