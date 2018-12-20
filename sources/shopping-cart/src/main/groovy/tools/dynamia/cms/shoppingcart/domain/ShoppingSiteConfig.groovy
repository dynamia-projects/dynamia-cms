/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
