package com.dynamia.cms.site.shoppingcart.domains;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.users.domain.UserContactInfo;
import com.dynamia.tools.domain.BaseEntity;
import com.dynamia.tools.domain.contraints.NotEmpty;

@Entity
@Table(name = "sc_orders", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "site_id", "number" })
})
public class ShoppingOrder extends BaseEntity implements SiteAware {

	@OneToOne
	@NotNull
	private Site site;

	@NotNull
	@NotEmpty
	private String number;

	private String invoiceNumber;
	private String invoiceId;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private ShoppingCart shoppingCart;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private PaymentTransaction transaction;

	@OneToOne
	private UserContactInfo shippingAddress;
	@OneToOne
	private UserContactInfo billingAddress;

	@Column(length = 5000)
	private String userComments;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public PaymentTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(PaymentTransaction transaction) {
		this.transaction = transaction;
	}

	public UserContactInfo getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(UserContactInfo shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public UserContactInfo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(UserContactInfo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public boolean isCompleted() {
		return transaction != null && transaction.getStatus() == PaymentTransactionStatus.COMPLETED;
	}

	public void sync() {
		if (shoppingCart != null && transaction != null) {
			shoppingCart.compute();
			transaction.setAmount(shoppingCart.getTotalPrice());
			transaction.setTaxes(shoppingCart.getTotalTaxes());
			if (transaction.getTaxes() != null && transaction.getTaxes().longValue() > 0) {
				transaction.setTaxesBase(shoppingCart.getSubtotal());
			}
		}

	}

}
