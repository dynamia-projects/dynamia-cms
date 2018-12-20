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

package tools.dynamia.cms.shopping.admin.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.api.CMSListener
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.services.PaymentService
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
