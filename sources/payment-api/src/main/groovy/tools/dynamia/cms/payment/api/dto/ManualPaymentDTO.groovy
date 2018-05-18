package tools.dynamia.cms.payment.api.dto

class ManualPaymentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5337831315385025566L
    private Date creationDate
    private Date creationTime
    private String number
    private String source
    private String registrator
    private String registratorCode
    private String registratorId
    private String payer
    private String payerCode
    private String payerId
    private String reference
    private String reference2
    private String reference3
    private BigDecimal amount
    private String type
    private String description
    private Map<String, String> params = new HashMap<>()

    Map<String, String> getParams() {
		return params
    }

    void setParams(Map<String, String> params) {
		this.params = params
    }

    Date getCreationTime() {
		return creationTime
    }

    void setCreationTime(Date creationTime) {
		this.creationTime = creationTime
    }

    Date getCreationDate() {
		return creationDate
    }

    void setCreationDate(Date creationDate) {
		this.creationDate = creationDate
    }

    String getNumber() {
		return number
    }

    void setNumber(String number) {
		this.number = number
    }

    String getSource() {
		return source
    }

    void setSource(String source) {
		this.source = source
    }

    String getRegistrator() {
		return registrator
    }

    void setRegistrator(String registrator) {
		this.registrator = registrator
    }

    String getRegistratorCode() {
		return registratorCode
    }

    void setRegistratorCode(String registratorCode) {
		this.registratorCode = registratorCode
    }

    String getRegistratorId() {
		return registratorId
    }

    void setRegistratorId(String registratorId) {
		this.registratorId = registratorId
    }

    String getPayer() {
		return payer
    }

    void setPayer(String payer) {
		this.payer = payer
    }

    String getPayerCode() {
		return payerCode
    }

    void setPayerCode(String payerCode) {
		this.payerCode = payerCode
    }

    String getPayerId() {
		return payerId
    }

    void setPayerId(String payerId) {
		this.payerId = payerId
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

    BigDecimal getAmount() {
		return amount
    }

    void setAmount(BigDecimal amount) {
		this.amount = amount
    }

    String getType() {
		return type
    }

    void setType(String type) {
		this.type = type
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

}
