package tools.dynamia.cms.site.products

import tools.dynamia.cms.site.products.domain.ProductReview

class ProductsReviewForm implements Serializable {

	private List<ProductReview> reviews = new ArrayList<>()

    ProductsReviewForm() {
		// TODO Auto-generated constructor stub
	}

    ProductsReviewForm(List<ProductReview> reviews) {
		super()
        this.reviews = reviews
    }

    List<ProductReview> getReviews() {
		return reviews
    }

    void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews
    }

}
