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

package tools.dynamia.cms.payment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.payment.services.impl.PaymentServiceImpl
import tools.dynamia.domain.EntityReference
import tools.dynamia.domain.EntityReferenceRepository
import tools.dynamia.integration.Containers

@Configuration
class PaymentConfig {


    @Bean
    EntityReferenceRepository<String> paymentGatewayEntiyReference() {
        return new EntityReferenceRepository<String>() {
            @Override
            String getAlias() {
                return "PaymentGateway"
            }

            @Override
            String getEntityClassName() {
                return "tools.dynamia.cms.payment.PaymentGateway"
            }

            @Override
            EntityReference<String> load(String id) {
                PaymentService service = new PaymentServiceImpl()
                PaymentGateway paymentGateway = service.findGateway(id)
                if (paymentGateway != null) {
                    return new EntityReference<String>(id, paymentGateway.class.name, paymentGateway.toString())
                }
                return null
            }

            @Override
            EntityReference<String> load(String field, Object value) {
                return getFirst()
            }

            @Override
            List<EntityReference<String>> find(String text, Map<String, Object> params) {
                return Containers.get().findObjects(PaymentGateway).collect {
                    new EntityReference<String>(it.id, it.class.name, it.toString())
                }
            }

            @Override
            EntityReference<String> getFirst() {
                return find("", [:]).first()
            }
        }
    }

}
