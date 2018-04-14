package tools.dynamia.cms.site.payment.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ManualPaymentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5337831315385025566L;
	private Date creationDate;
	private Date creationTime;
	private String number;
	private String source;
	private String registrator;
	private String registratorCode;
	private String registratorId;
	private String payer;
	private String payerCode;
	private String payerId;
	private String reference;
	private String reference2;
	private String reference3;
	private BigDecimal amount;
	private String type;
	private String description;
	private Map<String, String> params = new HashMap<>();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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
