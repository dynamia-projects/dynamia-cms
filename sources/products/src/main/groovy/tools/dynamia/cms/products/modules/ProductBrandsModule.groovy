package tools.dynamia.cms.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductBrandsModule extends AbstractModule {

	@Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
	private CrudService crudService

    ProductBrandsModule() {
		super("products_brands", "Products Brands", "products/modules/brandslist")
        description = "Show a products brands list"
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "20-04-2017")
        variablesNames = "brands"

    }

	@Override
    void init(ModuleContext context) {
		List<ProductBrand> brands = null

        if (brands == null || brands.empty) {
			brands = service.getBrands(context.site)
        }

        context.moduleInstance.addObject("brands", brands)

    }

}
