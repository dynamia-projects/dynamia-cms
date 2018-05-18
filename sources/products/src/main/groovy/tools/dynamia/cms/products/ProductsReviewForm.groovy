package tools.dynamia.cms.products

class ProductsReviewForm implements Serializable {

	private List<tools.dynamia.cms.products.domain.ProductReview> reviews = new ArrayList<>()

    ProductsReviewForm() {
		// TODO Auto-generated constructor stub
	}

    ProductsReviewForm(List<tools.dynamia.cms.products.domain.ProductReview> reviews) {
		super()
        this.reviews = reviews
    }

    List<tools.dynamia.cms.products.domain.ProductReview> getReviews() {
		return reviews
    }

    void setReviews(List<tools.dynamia.cms.products.domain.ProductReview> reviews) {
		this.reviews = reviews
    }

}
