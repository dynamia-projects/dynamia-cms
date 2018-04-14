package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductCategoriesFiltersModule extends AbstractModule {

	@Autowired
	private ProductsService service

    @Autowired
	private CrudService crudService

    ProductCategoriesFiltersModule() {
		super("products_categories_filters", "Products Categories Filters", "products/modules/categoryfilters")
        setDescription("Show a products categories filters")
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "13-03-2017")

    }

	@Override
    void init(ModuleContext context) {
			

	}

}
