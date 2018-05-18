package tools.dynamia.cms.shopping.admin.controllers

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.StringUtils
import tools.dynamia.integration.Containers
import tools.dynamia.zk.crud.CrudController

class PaymentGatewayConfigController extends CrudController<PaymentGatewayConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8595110206029594544L

    @Override
	protected void beforeQuery() {
		ShoppingCartService cartService = Containers.get().findObject(ShoppingCartService.class)
        ShoppingSiteConfig shopConfig = cartService.getConfiguration(SiteContext.get().current)

        PaymentService service = Containers.get().findObject(PaymentService.class)

        PaymentGateway gateway = null

        if (shopConfig != null && shopConfig.paymentGatewayId != null) {
			String gatewayId = shopConfig.paymentGatewayId
            gateway = service.findGateway(gatewayId)
        }

		if (gateway == null) {
			gateway = service.defaultGateway
        }

		String source = SiteContext.get().current.key
        if (gateway != null) {

			for (String param : (gateway.requiredParams)) {
				PaymentGatewayConfig config = service.getConfig(gateway, param, source)
                if (config == null) {
					String label = StringUtils.capitalize(param)
                    label = StringUtils.addSpaceBetweenWords(label)
                    config = new PaymentGatewayConfig(gateway, param, "", source, label)
                    crudService.create(config)
                }
			}
		}

	}

}
