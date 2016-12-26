package tools.dynamia.cms.site.payment.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.BaseEntity;

@Table(name = "pay_manuals")
@Entity
public class ManualPayment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1369368571148885123L;

	private String uuid = StringUtils.randomString().toUpperCase();

	private String number;

	@NotNull(message = "Manual Payment source invalid")
	private String source;
	private String registrator;
	private String registratorCode;
	private String registratorId;

	@Column(length = 1000)
	private String payer;
	private String payerCode;
	private String payerId;
	@Column(length = 3000)
	private String reference;
	@Column(length = 3000)
	private String reference2;
	@Column(length = 3000)
	private String reference3;

	@NotNull(message = "Payment amount is required")
	private BigDecimal amount;
	private String type;

	@Column(length = 3000)
	private String description;

	private boolean sended;
	private String errorCode;
	private String errorMessage;
	private String externalRef;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
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

	public void setErrorMessage(String erroMessage) {
		this.errorMessage = erroMessage;
	}

	public boolean isSended() {
		return sended;
	}

	public void setSended(boolean sended) {
		this.sended = sended;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRegistrator() {
		return registrator;
	}

	public void setRegistrator(String registrator) {
		this.registrator = registrator;
	}

	public String getRegistratorCode() {
		return registratorCode;
	}

	public void setRegistratorCode(String registratorCode) {
		this.registratorCode = registratorCode;
	}

	public String getRegistratorId() {
		return registratorId;
	}

	public void setRegistratorId(String registratorId) {
		this.registratorId = registratorId;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayerCode() {
		return payerCode;
	}

	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
