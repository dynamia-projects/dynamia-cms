package tools.dynamia.cms.admin.shopping.controllers

import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.integration.Containers
import tools.dynamia.zk.crud.CrudController

class ShoppingSiteConfigController extends CrudController<ShoppingSiteConfig> {

	@Override
	protected void beforeQuery() {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class)
        service.getConfiguration(SiteContext.get().current)
    }

}
