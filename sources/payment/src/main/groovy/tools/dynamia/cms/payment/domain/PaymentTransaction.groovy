/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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

}
