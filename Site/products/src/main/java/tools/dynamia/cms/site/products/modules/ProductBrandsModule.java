package tools.dynamia.cms.site.products.modules;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.api.AbstractModule;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.domain.services.CrudService;

@CMSModule
public class ProductBrandsModule extends AbstractModule {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	public ProductBrandsModule() {
		super("products_brands", "Products Brands", "products/modules/brandslist");
		setDescription("Show a products brands list");
		putMetadata("author", "Mario Serrano Leones");
		putMetadata("version", "1.0");
		putMetadata("created at", "20-04-2017");
		setVariablesNames("brands");

	}

	@Override
	public void init(ModuleContext context) {
		List<ProductBrand> brands = null;

		if (brands == null || brands.isEmpty()) {
			brands = service.getBrands(context.getSite());
		}

		context.getModuleInstance().addObject("brands", brands);

	}

}
