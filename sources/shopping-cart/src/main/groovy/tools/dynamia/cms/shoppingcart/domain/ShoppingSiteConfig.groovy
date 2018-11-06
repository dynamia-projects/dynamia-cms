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
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "sc_configuration")
class ShoppingSiteConfig extends SimpleEntity implements SiteAware {

	@NotNull
	@OneToOne
	Site site
    @OneToOne
    PaymentGatewayAccount paymentGatewayAccount;
    boolean paymentEnabled
    boolean allowEmptyPayment
    boolean billingAddressRequired
    boolean shippingAddressRequired

    BigDecimal minPaymentAmount
    BigDecimal maxPaymentAmount
    float shipmentPercent
    BigDecimal minShipmentAmount
    String defaultCurrency
    String descriptionTemplate
    String notificationEmails
    String orderSenderURL
    boolean autoSendOrders
    String orderStatusURL
    String paymentsSenderURL
    boolean autoSendPayments
    int minQuantityByProducts
    int minQuantityByCart

    @OneToOne
	MailTemplate orderCompletedMailTemplate
    @OneToOne
	MailTemplate orderShippedMailTemplate
    @OneToOne
	MailTemplate notificationMailTemplate
    @OneToOne
	MailAccount mailAccount

    @OneToMany(mappedBy = "siteConfig", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<ShoppingSiteConfigParameter> parameters = new ArrayList<>()

    @Column(length = 3000)
	String paymentTypes


    BigDecimal getMinShipmentAmount() {
		if (minShipmentAmount == null) {
			minShipmentAmount = BigDecimal.ZERO
        }
		return minShipmentAmount
    }


    BigDecimal getMaxPaymentAmount() {
		if (maxPaymentAmount == null) {
			maxPaymentAmount = BigDecimal.ZERO
        }
		return maxPaymentAmount
    }


    BigDecimal getMinPaymentAmount() {
		if (minPaymentAmount == null) {
			minPaymentAmount = BigDecimal.ZERO
        }
		return minPaymentAmount
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
