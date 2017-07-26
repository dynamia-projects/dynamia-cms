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
package tools.dynamia.cms.site.shoppingcart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailTemplate;
import tools.dynamia.domain.SimpleEntity;

@Entity
@Table(name = "sc_configuration")
public class ShoppingSiteConfig extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	private Site site;
	private String paymentGatewayId;
	private String paymentGatewayName;
	private boolean paymentEnabled;
	private boolean allowEmptyPayment;
	private boolean billingAddressRequired;
	private boolean shippingAddressRequired;

	private BigDecimal minPaymentAmount;
	private BigDecimal maxPaymentAmount;
	private float shipmentPercent;
	private BigDecimal minShipmentAmount;
	private String defaultCurrency;
	private String descriptionTemplate;
	private String notificationEmails;
	private String orderSenderURL;
	private boolean autoSendOrders;
	private String orderStatusURL;
	private String paymentsSenderURL;
	private boolean autoSendPayments;
	private int minQuantityByProducts;
	private int minQuantityByCart;

	@OneToOne
	private MailTemplate orderCompletedMailTemplate;
	@OneToOne
	private MailTemplate orderShippedMailTemplate;
	@OneToOne
	private MailTemplate notificationMailTemplate;
	@OneToOne
	private MailAccount mailAccount;

	@OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ShoppingSiteConfigParameter> parameters = new ArrayList<>();

	@Column(length = 3000)
	private String paymentTypes;

	public int getMinQuantityByProducts() {
		return minQuantityByProducts;
	}

	public void setMinQuantityByProducts(int minQuantityByProducts) {
		this.minQuantityByProducts = minQuantityByProducts;
	}

	public int getMinQuantityByCart() {
		return minQuantityByCart;
	}

	public void setMinQuantityByCart(int minQuantityByCart) {
		this.minQuantityByCart = minQuantityByCart;
	}

	public String getPaymentsSenderURL() {
		return paymentsSenderURL;
	}

	public void setPaymentsSenderURL(String paymentsSenderURL) {
		this.paymentsSenderURL = paymentsSenderURL;
	}

	public boolean isAutoSendPayments() {
		return autoSendPayments;
	}

	public void setAutoSendPayments(boolean autoSendPayments) {
		this.autoSendPayments = autoSendPayments;
	}

	public String getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(String paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	public List<ShoppingSiteConfigParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ShoppingSiteConfigParameter> parameters) {
		this.parameters = parameters;
	}

	public String getOrderStatusURL() {
		return orderStatusURL;
	}

	public void setOrderStatusURL(String orderStatusURL) {
		this.orderStatusURL = orderStatusURL;
	}

	public boolean isBillingAddressRequired() {
		return billingAddressRequired;
	}

	public void setBillingAddressRequired(boolean billingAddressRequired) {
		this.billingAddressRequired = billingAddressRequired;
	}

	public boolean isShippingAddressRequired() {
		return shippingAddressRequired;
	}

	public void setShippingAddressRequired(boolean shippingAddressRequired) {
		this.shippingAddressRequired = shippingAddressRequired;
	}

	public boolean isAutoSendOrders() {
		return autoSendOrders;
	}

	public void setAutoSendOrders(boolean autoSendOrders) {
		this.autoSendOrders = autoSendOrders;
	}

	public boolean isAllowEmptyPayment() {
		return allowEmptyPayment;
	}

	public void setAllowEmptyPayment(boolean allowEmptyPayment) {
		this.allowEmptyPayment = allowEmptyPayment;
	}

	public String getOrderSenderURL() {
		return orderSenderURL;
	}

	public void setOrderSenderURL(String orderSenderURL) {
		this.orderSenderURL = orderSenderURL;
	}

	public BigDecimal getMinShipmentAmount() {
		if (minShipmentAmount == null) {
			minShipmentAmount = BigDecimal.ZERO;
		}
		return minShipmentAmount;
	}

	public void setMinShipmentAmount(BigDecimal minShipmentAmount) {
		this.minShipmentAmount = minShipmentAmount;
	}

	public BigDecimal getMaxPaymentAmount() {
		if (maxPaymentAmount == null) {
			maxPaymentAmount = BigDecimal.ZERO;
		}
		return maxPaymentAmount;
	}

	public void setMaxPaymentAmount(BigDecimal maxPaymentAmount) {
		this.maxPaymentAmount = maxPaymentAmount;
	}

	public float getShipmentPercent() {
		return shipmentPercent;
	}

	public void setShipmentPercent(float shipmentPercent) {
		this.shipmentPercent = shipmentPercent;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public MailTemplate getOrderCompletedMailTemplate() {
		return orderCompletedMailTemplate;
	}

	public void setOrderCompletedMailTemplate(MailTemplate orderCompletedMailTemplate) {
		this.orderCompletedMailTemplate = orderCompletedMailTemplate;
	}

	public MailTemplate getOrderShippedMailTemplate() {
		return orderShippedMailTemplate;
	}

	public void setOrderShippedMailTemplate(MailTemplate orderShippedMailTemplate) {
		this.orderShippedMailTemplate = orderShippedMailTemplate;
	}

	public MailTemplate getNotificationMailTemplate() {
		return notificationMailTemplate;
	}

	public void setNotificationMailTemplate(MailTemplate notificationMailTemplate) {
		this.notificationMailTemplate = notificationMailTemplate;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getDescriptionTemplate() {
		return descriptionTemplate;
	}

	public String getNotificationEmails() {
		return notificationEmails;
	}

	public void setNotificationEmails(String notificationEmails) {
		this.notificationEmails = notificationEmails;
	}

	public void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate;
	}

	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}

	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}

	public String getPaymentGatewayName() {
		return paymentGatewayName;
	}

	public void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName;
	}

	public boolean isPaymentEnabled() {
		return paymentEnabled;
	}

	public void setPaymentEnabled(boolean paymentEnabled) {
		this.paymentEnabled = paymentEnabled;
	}

	public BigDecimal getMinPaymentAmount() {
		if (minPaymentAmount == null) {
			minPaymentAmount = BigDecimal.ZERO;
		}
		return minPaymentAmount;
	}

	public void setMinPaymentAmount(BigDecimal minPaymentAmount) {
		this.minPaymentAmount = minPaymentAmount;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public void addItem(ShoppingCartItem item, int qty) {
	}

	public Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>();
		for (ShoppingSiteConfigParameter param : getParameters()) {
			params.put(param.getName(), param.getValue());
		}
		return params;
	}

}
