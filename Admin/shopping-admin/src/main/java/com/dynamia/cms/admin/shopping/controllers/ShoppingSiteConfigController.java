package com.dynamia.cms.admin.shopping.controllers;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.web.crud.CrudController;

public class ShoppingSiteConfigController extends CrudController<ShoppingSiteConfig> {

	@Override
	protected void beforeQuery() {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class);
		service.getConfiguration(SiteContext.get().getCurrent());
	}

}
