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
package tools.dynamia.cms.payment.domain

import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.Reference
import tools.dynamia.domain.SimpleEntity

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "pay_gateway_config")
class PaymentGatewayConfig extends SimpleEntity {


    @ManyToOne
    PaymentGatewayAccount account
    @NotNull
    @Reference("PaymenteGateway")
    String gatewayId
    String name
    String value
    String source
    String label

    PaymentGatewayConfig() {
        // TODO Auto-generated constructor stub
    }

    PaymentGatewayConfig(PaymentGateway gateway, String name, String value) {
        this.gatewayId = gateway.id
        this.name = name
        this.value = value
        this.label = StringUtils.capitalizeAllWords(StringUtils.addSpaceBetweenWords(name))
    }

    PaymentGatewayConfig(PaymentGateway gateway, String name, String value, String source) {
        this.gatewayId = gateway.id
        this.name = name
        this.value = value
        this.source = source
        this.label = StringUtils.capitalizeAllWords(StringUtils.addSpaceBetweenWords(name))
    }

    PaymentGatewayConfig(PaymentGateway gateway, String name, String value, String source, String label) {
        super()
        this.gatewayId = gateway.id
        this.name = name
        this.value = value
        this.source = source
        this.label = label
    }

}
