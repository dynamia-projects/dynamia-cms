package com.dynamia.cms.site.payment.domain;

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

import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.BaseEntity;

@Entity
@Table(name = "pay_transactions")
public class PaymentTransaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6395335361885553112L;

	private String uuid = StringUtils.randomString();

	@NotNull
	private String gatewayId;

	@Enumerated(EnumType.ORDINAL)
	private PaymentTransactionStatus status = PaymentTransactionStatus.NEW;
	private String statusText;
	@Lob
	@Basic(fetch = FetchType.LAZY)
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
	private String signature;
	@Column(length = 4000)
	private String description;
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

	public PaymentTransaction() {
		// TODO Auto-generated constructor stub
	}

	public PaymentTransaction(String source) {
		super();
		this.source = source;
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
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
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
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTaxesBase() {
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
		return "PaymentTransaction [uuid=" + uuid + ", gatewayId=" + gatewayId
				+ ", status=" + status + ", statusText=" + statusText + ", amount=" + amount
				+ ", taxes=" + taxes + ", taxesBase=" + taxesBase + ", paymentMethod=" + paymentMethod + ", currency=" + currency
				+ ", responseCode=" + responseCode + ", signature=" + signature + ", description=" + description + ", test=" + test
				+ ", email=" + email + "]";
	}

	public boolean isValidSignature(String responseSignature) {
		try {
			return this.signature.equals(responseSignature);
		} catch (Exception e) {
			return false;
		}
	}

}