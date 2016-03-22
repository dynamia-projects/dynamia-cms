/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.shoppingcart.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.users.domain.UserContactInfo;

import tools.dynamia.domain.BaseEntity;
import tools.dynamia.domain.contraints.NotEmpty;

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
	private String trackingNumber;
	private ShippingCompany shippingCompany;
	@Temporal(TemporalType.DATE)
	private Date estimatedArrivalDate;
	private String shippingComments;
	@Temporal(TemporalType.TIMESTAMP)
	private Date shippingDate;
	private boolean shipped;

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

	private boolean pickupAtStore;
	private boolean payAtDelivery;

	@Column(length = 5000)
	private String userComments;

	public boolean isPickupAtStore() {

		return pickupAtStore;
	}

	public void setPickupAtStore(boolean pickupAtStore) {
		this.pickupAtStore = pickupAtStore;
	}

	public boolean isPayAtDelivery() {
		return payAtDelivery;
	}

	public void setPayAtDelivery(boolean payAtDelivery) {
		this.payAtDelivery = payAtDelivery;
	}

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public String getShippingComments() {
		return shippingComments;
	}

	public void setShippingComments(String shippingComments) {
		this.shippingComments = shippingComments;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public ShippingCompany getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(ShippingCompany shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public Date getEstimatedArrivalDate() {
		return estimatedArrivalDate;
	}

	public void setEstimatedArrivalDate(Date estimatedArrivalDate) {
		this.estimatedArrivalDate = estimatedArrivalDate;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

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
			syncTransaction();
		}

	}

	public void syncTransaction() {
		if (shoppingCart != null && transaction != null) {
			transaction.setAmount(shoppingCart.getTotalPrice());
			transaction.setTaxes(shoppingCart.getTotalTaxes());
			if (transaction.getTaxes() != null && transaction.getTaxes().longValue() > 0) {
				transaction.setTaxesBase(shoppingCart.getSubtotal());
			}
		}
	}

}
