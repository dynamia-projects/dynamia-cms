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
