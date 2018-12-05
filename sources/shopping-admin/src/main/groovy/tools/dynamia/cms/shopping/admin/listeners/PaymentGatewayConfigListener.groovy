/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
