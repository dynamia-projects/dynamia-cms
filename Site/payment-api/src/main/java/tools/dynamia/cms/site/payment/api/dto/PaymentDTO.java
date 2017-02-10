package tools.dynamia.cms.site.payment.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus;

public class PaymentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5473703780991784222L;
	private String uuid;
	private String gatewayId;
	private PaymentTransactionStatus status = PaymentTransactionStatus.NEW;
	private String statusText;
	private Date statusDate;
	@JsonIgnore
	private String gatewayResponse;
	private Date startDate = new Date();
	private Date endDate;
	private BigDecimal amount;
	private BigDecimal taxes;
	private BigDecimal taxesBase;
	private String paymentMethod;
	private String currency;
	private String responseCode;
	@JsonIgnore
	private String signature;
	private String description;
	private String payerFullname;
	private String payerDocument;
	private String payerPhoneNumber;
	private String payerMobileNumber;
	private String email;
	private String source;
	private String bank;
	private String reference;
	private String reference2;
	private String reference3;
	private String clientIP;
	private String responseURL;
	private String confirmationURL;
	private boolean test = false;
	private boolean confirmed;

	private int responseTries;
	private String lastStatusText;
	private PaymentTransactionStatus lastStatus;

	private Date lastStatusDate;
	private Map<String, String> params;

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
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

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getGatewayResponse() {
		return gatewayResponse;
	}

	public void setGatewayResponse(String gatewayResponse) {
		this.gatewayResponse = gatewayResponse;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
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

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
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

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
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

}
