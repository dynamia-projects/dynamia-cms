package tools.dynamia.cms.site.products

import tools.dynamia.cms.site.products.domain.ProductReview

public class ProductsReviewForm implements Serializable {

	private List<ProductReview> reviews = new ArrayList<>();

	public ProductsReviewForm() {
		// TODO Auto-generated constructor stub
	}

	public ProductsReviewForm(List<ProductReview> reviews) {
		super();
		this.reviews = reviews;
	}

	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

}
