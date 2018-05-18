package tools.dynamia.cms.products.listeners

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.products.domain.ProductReview
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

@Listener
class ProductReviewCrudListener extends CrudServiceListenerAdapter<ProductReview> {

	@Autowired
	private ProductsService service

    @Override
    void afterCreate(ProductReview entity) {
		try {
			service.computeProductStars(entity.product)
        } catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
    void afterUpdate(ProductReview entity) {
		afterCreate(entity)
    }
}
