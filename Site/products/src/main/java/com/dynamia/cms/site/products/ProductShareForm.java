package com.dynamia.cms.site.products;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.contraints.Email;
import com.dynamia.tools.domain.contraints.NotEmpty;

public class ProductShareForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7043302060479353034L;
	@Email(message = "Ingrese email valido")
	private String friendEmail;
	private String friendName;
	@NotEmpty
	private String yourName;
	@NotEmpty
	private String message;
	private String productURL;
	@NotNull
	private Long productId;
	private Site site;

	public ProductShareForm() {
		// TODO Auto-generated constructor stub
	}

	public ProductShareForm(Site site) {
		super();
		this.site = site;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getFriendEmail() {
		return friendEmail;
	}

	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}

	public String getYourName() {
		return yourName;
	}

	public void setYourName(String yourName) {
		this.yourName = yourName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
