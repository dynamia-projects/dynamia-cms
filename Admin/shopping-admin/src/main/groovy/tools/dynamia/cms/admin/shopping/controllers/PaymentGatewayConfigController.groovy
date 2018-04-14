package tools.dynamia.cms.admin.shopping.controllers

import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
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
        ShoppingSiteConfig shopConfig = cartService.getConfiguration(SiteContext.get().getCurrent())

        PaymentService service = Containers.get().findObject(PaymentService.class)

        PaymentGateway gateway = null

        if (shopConfig != null && shopConfig.getPaymentGatewayId() != null) {
			String gatewayId = shopConfig.getPaymentGatewayId()
            gateway = service.findGateway(gatewayId)
        }

		if (gateway == null) {
			gateway = service.getDefaultGateway()
        }

		String source = SiteContext.get().getCurrent().getKey()
        if (gateway != null) {

			for (String param : gateway.getRequiredParams()) {
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
