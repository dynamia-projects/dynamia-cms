package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

@CMSModule
public class ProductBrandsFiltersModule extends AbstractModule {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	public ProductBrandsFiltersModule() {
		super("products_brands_filters", "Products Brands Filters", "products/modules/brandsfilters");
		setDescription("Show a products brands filters");
		putMetadata("author", "Mario Serrano Leones");
		putMetadata("version", "1.0");
		putMetadata("created at", "13-03-2017");

	}

	@Override
	public void init(ModuleContext context) {
			

	}

}
