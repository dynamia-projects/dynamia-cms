/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.payment.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.BaseEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "pay_transactions")
class PaymentTransaction extends BaseEntity implements Payment {

    /**
     *
     */
    static final long serialVersionUID = 6395335361885553112L

    String uuid = StringUtils.randomString().toUpperCase()

    @NotNull
    String gatewayId
    @OneToOne
    @NotNull
    PaymentGatewayAccount paymentAccount
    @Enumerated(EnumType.ORDINAL)
    PaymentTransactionStatus status = PaymentTransactionStatus.NEW
    String statusText
    @Temporal(TemporalType.TIMESTAMP)
    Date statusDate
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    String gatewayResponse

    @Temporal(TemporalType.TIMESTAMP)
    Date startDate = new Date()
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate

    BigDecimal amount
    BigDecimal taxes
    BigDecimal taxesBase
    String paymentMethod
    String currency
    String responseCode
    @JsonIgnore
    String signature
    @Column(length = 1000)
    String description
    String document
    String payerFullname
    String payerDocument
    String payerCode
    String payerPhoneNumber
    String payerMobileNumber
    String email
    String source
    String bank
    String reference
    String reference2
    String reference3
    String clientIP
    @Column(length = 1000)
    String responseURL
    @Column(length = 1000)
    String confirmationURL
    boolean test = false
    boolean confirmed
    String baseURL

    int responseTries
    String lastStatusText
    PaymentTransactionStatus lastStatus
    @Temporal(TemporalType.TIMESTAMP)
    Date lastStatusDate

    boolean sended
    String errorCode
    String errorMessage
    String externalRef

    String shippingAddress
    String shippingCity
    String shippingCountry

    PaymentTransaction() {
        // TODO Auto-generated constructor stub
    }

    PaymentTransaction(String source) {
        super()
        this.source = source
    }


    void setStatus(PaymentTransactionStatus status) {
        if (this.status != status) {
            lastStatus = this.status
            lastStatusDate = this.statusDate
            this.statusDate = new Date()
        }

        this.status = status
    }


    void setStatusText(String statusText) {
        if (this.statusText != statusText) {
            this.lastStatusText = this.statusText
        }

        this.statusText = statusText
    }


    BigDecimal getAmount() {
        if (amount == null) {
            amount = BigDecimal.ZERO
        }
        return amount
    }


    BigDecimal getTaxes() {
        if (taxes == null) {
            taxes = BigDecimal.ZERO
        }
        return taxes
    }


    BigDecimal getTaxesBase() {
        if (taxesBase == null) {
            taxesBase = BigDecimal.ZERO
        }
        return taxesBase
    }


    @Override
    String toString() {
        return "PaymentTransaction from " + source + " --> [uuid=" + uuid + ", gatewayId=" + gatewayId + ", status="
        +status + ", statusText=" + statusText + ", amount=" + amount + ", taxes=" + taxes + ", taxesBase="
        +taxesBase + ", paymentMethod=" + paymentMethod + ", currency=" + currency + ", responseCode="
        +responseCode + ", signature=" + signature + ", description=" + description + ", test=" + test
        +", email=" + email + "]"
    }

    void checkStatusText() {
        if (statusText == null || statusText.empty) {
            statusText = status?.toString()
        }
    }
}
