/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.shoppingcart.admin.controllers

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
