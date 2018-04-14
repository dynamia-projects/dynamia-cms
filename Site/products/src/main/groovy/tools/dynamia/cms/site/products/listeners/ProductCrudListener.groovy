package tools.dynamia.cms.site.products.listeners;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Listener;

@Listener
public class ProductCrudListener extends CrudServiceListenerAdapter<Product> {

	@Autowired
	private ProductsService service;

	@Override
	public void afterCreate(Product entity) {
		updateProductsCounts();
	}

	@Override
	public void afterUpdate(Product entity) {
		updateProductsCounts();
	}

	private void updateProductsCounts() {
		try {
			service.computeProductCountByCategory(SiteContext.get().getCurrent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
