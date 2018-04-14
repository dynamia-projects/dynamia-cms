package tools.dynamia.cms.site.products.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.site.products.domain.ProductReview
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
public class ProductReviewCrudListener extends CrudServiceListenerAdapter<ProductReview> {

	@Autowired
	private ProductsService service;

	@Override
	public void afterCreate(ProductReview entity) {
		try {
			service.computeProductStars(entity.getProduct());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void afterUpdate(ProductReview entity) {
		afterCreate(entity);
	}
}
