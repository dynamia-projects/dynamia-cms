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
package tools.dynamia.cms.shoppingcart.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "sc_configuration")
class ShoppingSiteConfig extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	private Site site
    private String paymentGatewayId
    private String paymentGatewayName
    private boolean paymentEnabled
    private boolean allowEmptyPayment
    private boolean billingAddressRequired
    private boolean shippingAddressRequired

    private BigDecimal minPaymentAmount
    private BigDecimal maxPaymentAmount
    private float shipmentPercent
    private BigDecimal minShipmentAmount
    private String defaultCurrency
    private String descriptionTemplate
    private String notificationEmails
    private String orderSenderURL
    private boolean autoSendOrders
    private String orderStatusURL
    private String paymentsSenderURL
    private boolean autoSendPayments
    private int minQuantityByProducts
    private int minQuantityByCart

    @OneToOne
	private MailTemplate orderCompletedMailTemplate
    @OneToOne
	private MailTemplate orderShippedMailTemplate
    @OneToOne
	private MailTemplate notificationMailTemplate
    @OneToOne
	private MailAccount mailAccount

    @OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ShoppingSiteConfigParameter> parameters = new ArrayList<>()

    @Column(length = 3000)
	private String paymentTypes

    int getMinQuantityByProducts() {
		return minQuantityByProducts
    }

    void setMinQuantityByProducts(int minQuantityByProducts) {
		this.minQuantityByProducts = minQuantityByProducts
    }

    int getMinQuantityByCart() {
		return minQuantityByCart
    }

    void setMinQuantityByCart(int minQuantityByCart) {
		this.minQuantityByCart = minQuantityByCart
    }

    String getPaymentsSenderURL() {
		return paymentsSenderURL
    }

    void setPaymentsSenderURL(String paymentsSenderURL) {
		this.paymentsSenderURL = paymentsSenderURL
    }

    boolean isAutoSendPayments() {
		return autoSendPayments
    }

    void setAutoSendPayments(boolean autoSendPayments) {
		this.autoSendPayments = autoSendPayments
    }

    String getPaymentTypes() {
		return paymentTypes
    }

    void setPaymentTypes(String paymentTypes) {
		this.paymentTypes = paymentTypes
    }

    List<ShoppingSiteConfigParameter> getParameters() {
		return parameters
    }

    void setParameters(List<ShoppingSiteConfigParameter> parameters) {
		this.parameters = parameters
    }

    String getOrderStatusURL() {
		return orderStatusURL
    }

    void setOrderStatusURL(String orderStatusURL) {
		this.orderStatusURL = orderStatusURL
    }

    boolean isBillingAddressRequired() {
		return billingAddressRequired
    }

    void setBillingAddressRequired(boolean billingAddressRequired) {
		this.billingAddressRequired = billingAddressRequired
    }

    boolean isShippingAddressRequired() {
		return shippingAddressRequired
    }

    void setShippingAddressRequired(boolean shippingAddressRequired) {
		this.shippingAddressRequired = shippingAddressRequired
    }

    boolean isAutoSendOrders() {
		return autoSendOrders
    }

    void setAutoSendOrders(boolean autoSendOrders) {
		this.autoSendOrders = autoSendOrders
    }

    boolean isAllowEmptyPayment() {
		return allowEmptyPayment
    }

    void setAllowEmptyPayment(boolean allowEmptyPayment) {
		this.allowEmptyPayment = allowEmptyPayment
    }

    String getOrderSenderURL() {
		return orderSenderURL
    }

    void setOrderSenderURL(String orderSenderURL) {
		this.orderSenderURL = orderSenderURL
    }

    BigDecimal getMinShipmentAmount() {
		if (minShipmentAmount == null) {
			minShipmentAmount = BigDecimal.ZERO
        }
		return minShipmentAmount
    }

    void setMinShipmentAmount(BigDecimal minShipmentAmount) {
		this.minShipmentAmount = minShipmentAmount
    }

    BigDecimal getMaxPaymentAmount() {
		if (maxPaymentAmount == null) {
			maxPaymentAmount = BigDecimal.ZERO
        }
		return maxPaymentAmount
    }

    void setMaxPaymentAmount(BigDecimal maxPaymentAmount) {
		this.maxPaymentAmount = maxPaymentAmount
    }

    float getShipmentPercent() {
		return shipmentPercent
    }

    void setShipmentPercent(float shipmentPercent) {
		this.shipmentPercent = shipmentPercent
    }

    MailAccount getMailAccount() {
		return mailAccount
    }

    void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount
    }

    MailTemplate getOrderCompletedMailTemplate() {
		return orderCompletedMailTemplate
    }

    void setOrderCompletedMailTemplate(MailTemplate orderCompletedMailTemplate) {
		this.orderCompletedMailTemplate = orderCompletedMailTemplate
    }

    MailTemplate getOrderShippedMailTemplate() {
		return orderShippedMailTemplate
    }

    void setOrderShippedMailTemplate(MailTemplate orderShippedMailTemplate) {
		this.orderShippedMailTemplate = orderShippedMailTemplate
    }

    MailTemplate getNotificationMailTemplate() {
		return notificationMailTemplate
    }

    void setNotificationMailTemplate(MailTemplate notificationMailTemplate) {
		this.notificationMailTemplate = notificationMailTemplate
    }

    Site getSite() {
		return site
    }

    void setSite(Site site) {
		this.site = site
    }

    String getDescriptionTemplate() {
		return descriptionTemplate
    }

    String getNotificationEmails() {
		return notificationEmails
    }

    void setNotificationEmails(String notificationEmails) {
		this.notificationEmails = notificationEmails
    }

    void setDescriptionTemplate(String descriptionTemplate) {
		this.descriptionTemplate = descriptionTemplate
    }

    String getPaymentGatewayId() {
		return paymentGatewayId
    }

    void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId
    }

    String getPaymentGatewayName() {
		return paymentGatewayName
    }

    void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName
    }

    boolean isPaymentEnabled() {
		return paymentEnabled
    }

    void setPaymentEnabled(boolean paymentEnabled) {
		this.paymentEnabled = paymentEnabled
    }

    BigDecimal getMinPaymentAmount() {
		if (minPaymentAmount == null) {
			minPaymentAmount = BigDecimal.ZERO
        }
		return minPaymentAmount
    }

    void setMinPaymentAmount(BigDecimal minPaymentAmount) {
		this.minPaymentAmount = minPaymentAmount
    }

    String getDefaultCurrency() {
		return defaultCurrency
    }

    void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency
    }

    void addItem(ShoppingCartItem item, int qty) {
	}

    Map<String, String> getParametersAsMap() {
		Map<String, String> params = new HashMap<>()
        for (ShoppingSiteConfigParameter param : (parameters)) {
			params.put(param.name, param.value)
        }
		return params
    }

}
