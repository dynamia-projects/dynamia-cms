package tools.dynamia.cms.site.products.api;

import tools.dynamia.cms.site.products.dto.ProductsReviewResponse;

public interface ProductReviewsConnector {

	public ProductsReviewResponse requestReviews(String requestUuid);

}
