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
package com.dynamia.cms.admin.shoppingcart;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.shoppingcart.domain.ShippingCompany;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;

@CMSModule
public class ShoppingCartAdminModule implements AdminModule {

	@Override
	public String getGroup() {
		return "shopping";
	}

	@Override
	public String getName() {
		return "Shopping";
	}

	@Override
	public AdminModuleOption[] getOptions() {
		return new AdminModuleOption[] {
				new AdminModuleOption("shopConfig", "Configuration", ShoppingSiteConfig.class, false, true),
				new AdminModuleOption("paymentConfig", "Payment Gateway Config", PaymentGatewayConfig.class, false, true),
				new AdminModuleOption("shoppingOrders", "Orders", ShoppingOrder.class, true, true, "shopping-cart", true),
				new AdminModuleOption("shippingCompany", "Shipping Company", ShippingCompany.class)

		};
	}

}
