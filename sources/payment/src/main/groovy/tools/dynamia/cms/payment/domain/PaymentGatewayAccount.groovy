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

package tools.dynamia.cms.payment.domain

import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.Reference
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Entity
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
        def map = new HashMap<String,String>()
        configuration.each { map[it.name] = it.value }
        return map
    }

    @Override
    String toString() {
        return "$name ($gatewayId)"
    }
}
