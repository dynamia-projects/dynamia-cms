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

package tools.dynamia.cms.payment.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.payment.api.PaymentTransactionStatus

class PaymentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5473703780991784222L
    private String uuid
    private String gatewayId
    private PaymentTransactionStatus status = PaymentTransactionStatus.NEW
    private String statusText
    private Date statusDate
    @JsonIgnore
	private String gatewayResponse
    private Date startDate = new Date()
    private Date endDate
    private BigDecimal amount
    private BigDecimal taxes
    private BigDecimal taxesBase
    private String paymentMethod
    private String currency
    private String responseCode
    @JsonIgnore
	private String signature
    private String description
    private String document
    private String payerFullname
    private String payerDocument
    private String payerCode
    private String payerPhoneNumber
    private String payerMobileNumber
    private String email
    private String source
    private String bank
    private String reference
    private String reference2
    private String reference3
    private String clientIP
    private String responseURL
    private String confirmationURL
    private boolean test = false
    private boolean confirmed

    private int responseTries
    private String lastStatusText
    private PaymentTransactionStatus lastStatus

    private Date lastStatusDate
    private Map<String, String> params

    String getDocument() {
		return document
    }

    void setDocument(String document) {
		this.document = document
    }

    String getPayerCode() {
		return payerCode
    }

    void setPayerCode(String payerCode) {
		this.payerCode = payerCode
    }

    Map<String, String> getParams() {
		return params
    }

    void setParams(Map<String, String> params) {
		this.params = params
    }

    String getUuid() {
		return uuid
    }

    void setUuid(String uuid) {
		this.uuid = uuid
    }

    String getGatewayId() {
		return gatewayId
    }

    void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId
    }

    PaymentTransactionStatus getStatus() {
		return status
    }

    void setStatus(PaymentTransactionStatus status) {
		this.status = status
    }

    String getStatusText() {
		return statusText
    }

    void setStatusText(String statusText) {
		this.statusText = statusText
    }

    Date getStatusDate() {
		return statusDate
    }

    void setStatusDate(Date statusDate) {
		this.statusDate = statusDate
    }

    String getGatewayResponse() {
		return gatewayResponse
    }

    void setGatewayResponse(String gatewayResponse) {
		this.gatewayResponse = gatewayResponse
    }

    Date getStartDate() {
		return startDate
    }

    void setStartDate(Date startDate) {
		this.startDate = startDate
    }

    Date getEndDate() {
		return endDate
    }

    void setEndDate(Date endDate) {
		this.endDate = endDate
    }

    BigDecimal getAmount() {
		return amount
    }

    void setAmount(BigDecimal amount) {
		this.amount = amount
    }

    BigDecimal getTaxes() {
		return taxes
    }

    void setTaxes(BigDecimal taxes) {
		this.taxes = taxes
    }

    BigDecimal getTaxesBase() {
		return taxesBase
    }

    void setTaxesBase(BigDecimal taxesBase) {
		this.taxesBase = taxesBase
    }

    String getPaymentMethod() {
		return paymentMethod
    }

    void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod
    }

    String getCurrency() {
		return currency
    }

    void setCurrency(String currency) {
		this.currency = currency
    }

    String getResponseCode() {
		return responseCode
    }

    void setResponseCode(String responseCode) {
		this.responseCode = responseCode
    }

    String getSignature() {
		return signature
    }

    void setSignature(String signature) {
		this.signature = signature
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    String getPayerFullname() {
		return payerFullname
    }

    void setPayerFullname(String payerFullname) {
		this.payerFullname = payerFullname
    }

    String getPayerDocument() {
		return payerDocument
    }

    void setPayerDocument(String payerDocument) {
		this.payerDocument = payerDocument
    }

    String getPayerPhoneNumber() {
		return payerPhoneNumber
    }

    void setPayerPhoneNumber(String payerPhoneNumber) {
		this.payerPhoneNumber = payerPhoneNumber
    }

    String getPayerMobileNumber() {
		return payerMobileNumber
    }

    void setPayerMobileNumber(String payerMobileNumber) {
		this.payerMobileNumber = payerMobileNumber
    }

    String getEmail() {
		return email
    }

    void setEmail(String email) {
		this.email = email
    }

    String getSource() {
		return source
    }

    void setSource(String source) {
		this.source = source
    }

    String getBank() {
		return bank
    }

    void setBank(String bank) {
		this.bank = bank
    }

    String getReference() {
		return reference
    }

    void setReference(String reference) {
		this.reference = reference
    }

    String getReference2() {
		return reference2
    }

    void setReference2(String reference2) {
		this.reference2 = reference2
    }

    String getReference3() {
		return reference3
    }

    void setReference3(String reference3) {
		this.reference3 = reference3
    }

    String getClientIP() {
		return clientIP
    }

    void setClientIP(String clientIP) {
		this.clientIP = clientIP
    }

    String getResponseURL() {
		return responseURL
    }

    void setResponseURL(String responseURL) {
		this.responseURL = responseURL
    }

    String getConfirmationURL() {
		return confirmationURL
    }

    void setConfirmationURL(String confirmationURL) {
		this.confirmationURL = confirmationURL
    }

    boolean isTest() {
		return test
    }

    void setTest(boolean test) {
		this.test = test
    }

    boolean isConfirmed() {
		return confirmed
    }

    void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed
    }

    int getResponseTries() {
		return responseTries
    }

    void setResponseTries(int responseTries) {
		this.responseTries = responseTries
    }

    String getLastStatusText() {
		return lastStatusText
    }

    void setLastStatusText(String lastStatusText) {
		this.lastStatusText = lastStatusText
    }

    PaymentTransactionStatus getLastStatus() {
		return lastStatus
    }

    void setLastStatus(PaymentTransactionStatus lastStatus) {
		this.lastStatus = lastStatus
    }

    Date getLastStatusDate() {
		return lastStatusDate
    }

    void setLastStatusDate(Date lastStatusDate) {
		this.lastStatusDate = lastStatusDate
    }

}
