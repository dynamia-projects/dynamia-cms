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

import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.Reference
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "pay_gateway_accounts")
class PaymentGatewayAccount extends SimpleEntity {

    @NotEmpty
    String name
    boolean enabled = true
    String source
    @Reference("PaymentGateway")
    @NotNull
    String gatewayId
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PaymentGatewayConfig> configuration = new ArrayList<>()
    String uuid = StringUtils.randomString()


    void addConfig(PaymentGatewayConfig config) {
        configuration << config
        config.source = source
        config.account = this
    }

    Map<String, String> getConfigurationMap() {
        def map = new HashMap<String, String>()
        configuration.each { map[it.name] = it.value }
        return map
    }

    @Override
    String toString() {
        return "$name ($gatewayId)"
    }
}
