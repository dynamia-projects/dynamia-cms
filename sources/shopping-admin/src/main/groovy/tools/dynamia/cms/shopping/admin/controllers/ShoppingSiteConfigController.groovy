package tools.dynamia.cms.shopping.admin.controllers

import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.integration.Containers
import tools.dynamia.zk.crud.CrudController

class ShoppingSiteConfigController extends CrudController<ShoppingSiteConfig> {

	@Override
	protected void beforeQuery() {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class)
        service.getConfiguration(SiteContext.get().current)
    }

}
