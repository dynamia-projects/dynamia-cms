package tools.dynamia.cms.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductCategoriesFiltersModule extends AbstractModule {

	@Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
	private CrudService crudService

    ProductCategoriesFiltersModule() {
		super("products_categories_filters", "Products Categories Filters", "products/modules/categoryfilters")
        description = "Show a products categories filters"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "13-03-2017")

    }

	@Override
    void init(ModuleContext context) {
			

	}

}
