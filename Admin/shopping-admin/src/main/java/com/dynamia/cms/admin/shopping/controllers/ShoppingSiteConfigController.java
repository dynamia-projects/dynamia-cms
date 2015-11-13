package com.dynamia.cms.admin.shopping.controllers;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

import tools.dynamia.integration.Containers;
import tools.dynamia.zk.crud.CrudController;

public class ShoppingSiteConfigController extends CrudController<ShoppingSiteConfig> {

	@Override
	protected void beforeQuery() {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class);
		service.getConfiguration(SiteContext.get().getCurrent());
	}

}
