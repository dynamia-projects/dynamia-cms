package com.dynamia.cms.admin.shoppingcart;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;

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
				new AdminModuleOption("shopConfig", "Configuration", ShoppingSiteConfig.class),
				new AdminModuleOption("paymentConfig", "Payment Gateway Config", PaymentGatewayConfig.class)

		};
	}

}
