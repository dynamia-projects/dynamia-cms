package tools.dynamia.cms.site.products.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.domain.SiteBaseEntity;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 * Created by mario on 12/07/2017.
 */
@Entity
@Table(name = "prd_reviews")
public class ProductReview extends SiteBaseEntity {

	@OneToOne
	@NotNull
	private User user;

	@ManyToOne
	@NotNull
	@JsonIgnore
	private Product product;

	@NotEmpty
	@Column(length = 1000)
	private String comment;
	@Min(1)
	@Max(5)
	private int stars = 5;

	private boolean verified;
	private String document;
	private boolean incomplete;

	public boolean isIncomplete() {
		return incomplete;
	}

	public void setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int rate) {
		this.stars = rate;
	}

	@Override
	public String toString() {
		return String.format("%s review %s with %s stars and say %s", user, product, stars, comment);
	}
}
