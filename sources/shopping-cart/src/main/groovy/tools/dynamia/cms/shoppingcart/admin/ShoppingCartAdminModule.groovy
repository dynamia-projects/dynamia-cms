/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.shoppingcart.admin

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.shoppingcart.domain.ShippingCompany
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig

@CMSModule
class ShoppingCartAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "shopping"
    }

    @Override
    String getName() {
        return "Shopping"
    }

    @Override
    String getImage() {
        return "shopping-cart"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("shopConfig", "Configuration", ShoppingSiteConfig.class, false, true),
                new AdminModuleOption("shoppingOrders", "Orders", ShoppingOrder.class, true, true, "shopping-cart",
                        true),
                new AdminModuleOption("shippingCompany", "Shipping Company", ShippingCompany.class)

        ]
    }

}
