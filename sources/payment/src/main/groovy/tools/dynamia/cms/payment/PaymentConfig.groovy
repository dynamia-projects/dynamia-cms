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
