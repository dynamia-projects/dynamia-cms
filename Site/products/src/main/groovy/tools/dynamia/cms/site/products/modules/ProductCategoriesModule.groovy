package tools.dynamia.cms.site.products.modules

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.core.api.AbstractModule
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.core.api.ModuleContext
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter
import tools.dynamia.cms.site.products.domain.ProductBrand
import tools.dynamia.cms.site.products.domain.ProductCategory
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

@CMSModule
class ProductCategoriesModule extends AbstractModule {

	@Autowired
	private ProductsService service

    @Autowired
	private CrudService crudService

    ProductCategoriesModule() {
		super("products_categories", "Products Categories", "products/modules/categorylist")
        setDescription("Show a products categories list")
        putMetadata("author", "Mario Serrano Leones")
        putMetadata("version", "1.0")
        putMetadata("created at", "25-07-2016")
        setVariablesNames("categories")

    }

	@Override
    void init(ModuleContext context) {
		List<ProductCategory> categories = null

        ModuleInstanceParameter brandId = context.getParameter("brand")
        if (brandId != null) {
			ProductBrand brand = crudService.find(ProductBrand.class, new Long(brandId.getValue()))
            if (brand != null) {
				categories = service.getCategories(brand)
            }
		}

		try {
			long parentCategoryId = Long.parseLong(context.getParameterValue("parentCategory", "0"))
            if (parentCategoryId > 0) {
				ProductCategory parentCategory = crudService.find(ProductCategory.class, parentCategoryId)
                categories = service.getSubcategories(parentCategory)
            }
		} catch (Exception e) {
			e.printStackTrace()
        }

		if (categories == null || categories.isEmpty()) {
			categories = service.getCategories(context.getSite())
        }

		context.getModuleInstance().addObject("categories", categories)

    }

}
