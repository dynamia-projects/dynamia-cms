package tools.dynamia.cms.site.products.api

import tools.dynamia.cms.site.products.dto.ProductsReviewResponse

interface ProductReviewsConnector {

	ProductsReviewResponse requestReviews(String requestUuid)

}
