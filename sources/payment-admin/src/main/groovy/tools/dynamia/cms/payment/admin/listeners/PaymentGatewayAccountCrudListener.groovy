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

package tools.dynamia.cms.payment.admin.listeners

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
class PaymentGatewayAccountCrudListener extends CrudServiceListenerAdapter<PaymentGatewayAccount> {

    @Override
    void beforeCreate(PaymentGatewayAccount entity) {
        entity.source = SiteContext.get().current.key
    }

    @Override
    void beforeQuery(QueryParameters params) {

        if (params.getType() != null && BeanUtils.isAssignable(params.getType(), PaymentGatewayAccount.class)) {
            params.add("source", QueryConditions.eq(SiteContext.get().current.key))
        }

    }
}
