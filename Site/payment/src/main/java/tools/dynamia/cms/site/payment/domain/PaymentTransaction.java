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
package tools.dynamia.cms.site.payment.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.payment.api.Payment;
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.BaseEntity;

@Entity
@Table(name = "pay_transactions")
public class PaymentTransaction extends BaseEntity implements Payment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6395335361885553112L;

	private String uuid = StringUtils.randomString().toUpperCase();

	@NotNull
	private String gatewayId;

	@Enumerated(EnumType.ORDINAL)
	private PaymentTransactionStatus status = PaymentTransactionStatus.NEW;
	private String statusText;
	@Temporal(TemporalType.TIMESTAMP)
	private Date statusDate;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private String gatewayResponse;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private BigDecimal amount;
	private BigDecimal taxes;
	private BigDecimal taxesBase;
	private String paymentMethod;
	private String currency;
	private String responseCode;
	@JsonIgnore
	private String signature;
	@Column(length = 1000)
	private String description;
	private String document;
	private String payerFullname;
	private String payerDocument;
	private String payerCode;
	private String payerPhoneNumber;
	private String payerMobileNumber;
	private String email;
	private String source;
	private String bank;
	private String reference;
	private String reference2;
	private String reference3;
	private String clientIP;
	@Column(length = 1000)
	private String responseURL;
	@Column(length = 1000)
	private String confirmationURL;
	private boolean test = false;
	private boolean confirmed;

	private int responseTries;
	private String lastStatusText;
	private PaymentTransactionStatus lastStatus;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastStatusDate;

	private boolean sended;
	private String errorCode;
	private String errorMessage;
	private String externalRef;

	public PaymentTransaction() {
		// TODO Auto-generated constructor stub
	}

	public PaymentTransaction(String source) {
		super();
		this.source = source;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getPayerCode() {
		return payerCode;
	}

	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}

	public boolean isSended() {
		return sended;
	}

	public void setSended(boolean sended) {
		this.sended = sended;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public int getResponseTries() {
		return responseTries;
	}

	public void setResponseTries(int responseTries) {
		this.responseTries = responseTries;
	}

	public String getLastStatusText() {
		return lastStatusText;
	}

	public void setLastStatusText(String lastStatusText) {
		this.lastStatusText = lastStatusText;
	}

	public PaymentTransactionStatus getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(PaymentTransactionStatus lastStatus) {
		this.lastStatus = lastStatus;
	}

	public Date getLastStatusDate() {
		return lastStatusDate;
	}

	public void setLastStatusDate(Date lastStatusDate) {
		this.lastStatusDate = lastStatusDate;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}

	public String getConfirmationURL() {
		return confirmationURL;
	}

	public void setConfirmationURL(String confirmationURL) {
		this.confirmationURL = confirmationURL;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getPayerFullname() {
		return payerFullname;
	}

	public void setPayerFullname(String payerFullname) {
		this.payerFullname = payerFullname;
	}

	public String getPayerDocument() {
		return payerDocument;
	}

	public void setPayerDocument(String payerDocument) {
		this.payerDocument = payerDocument;
	}

	public String getPayerPhoneNumber() {
		return payerPhoneNumber;
	}

	public void setPayerPhoneNumber(String payerPhoneNumber) {
		this.payerPhoneNumber = payerPhoneNumber;
	}

	public String getPayerMobileNumber() {
		return payerMobileNumber;
	}

	public void setPayerMobileNumber(String payerMobileNumber) {
		this.payerMobileNumber = payerMobileNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public PaymentTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentTransactionStatus status) {
		if (this.status != status) {
			lastStatus = this.status;
			lastStatusDate = this.statusDate;
			this.statusDate = new Date();
		}

		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		if (this.statusText != statusText) {
			this.lastStatusText = this.statusText;
		}

		this.statusText = statusText;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getGatewayResponse() {
		return gatewayResponse;
	}

	public void setGatewayResponse(String gatewayResponse) {
		this.gatewayResponse = gatewayResponse;
	}

	public BigDecimal getAmount() {
		if (amount == null) {
			amount = BigDecimal.ZERO;
		}
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTaxes() {
		if (taxes == null) {
			taxes = BigDecimal.ZERO;
		}
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTaxesBase() {
		if (taxesBase == null) {
			taxesBase = BigDecimal.ZERO;
		}
		return taxesBase;
	}

	public void setTaxesBase(BigDecimal taxesBase) {
		this.taxesBase = taxesBase;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference2() {
		return reference2;
	}

	public void setReference2(String reference2) {
		this.reference2 = reference2;
	}

	public String getReference3() {
		return reference3;
	}

	public void setReference3(String reference3) {
		this.reference3 = reference3;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PaymentTransaction from " + source + " --> [uuid=" + uuid + ", gatewayId=" + gatewayId + ", status="
				+ status + ", statusText=" + statusText + ", amount=" + amount + ", taxes=" + taxes + ", taxesBase="
				+ taxesBase + ", paymentMethod=" + paymentMethod + ", currency=" + currency + ", responseCode="
				+ responseCode + ", signature=" + signature + ", description=" + description + ", test=" + test
				+ ", email=" + email + "]";
	}

}
