package toosl.dynamia.cms.site.shoppingcart.dto

class OrderStatusDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8443711900361650187L
    private String code
    private String description
    private double quantity
    private BigDecimal price
    private BigDecimal total
    private Date shipDate
    private String currency
    private BigDecimal priceVAT
    private BigDecimal vat
    private double vatPercent
    private BigDecimal totalVAT

    String getCode() {
		return code
    }

    void setCode(String code) {
		this.code = code
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    double getQuantity() {
		return quantity
    }

    void setQuantity(double quantity) {
		this.quantity = quantity
    }

    BigDecimal getPrice() {
		return price
    }

    void setPrice(BigDecimal price) {
		this.price = price
    }

    BigDecimal getTotal() {
		return total
    }

    void setTotal(BigDecimal total) {
		this.total = total
    }

    Date getShipDate() {
		return shipDate
    }

    void setShipDate(Date shipDate) {
		this.shipDate = shipDate
    }

    String getCurrency() {
		return currency
    }

    void setCurrency(String currency) {
		this.currency = currency
    }

    BigDecimal getPriceVAT() {
		return priceVAT
    }

    void setPriceVAT(BigDecimal priceVAT) {
		this.priceVAT = priceVAT
    }

    BigDecimal getVat() {
		return vat
    }

    void setVat(BigDecimal vat) {
		this.vat = vat
    }

    double getVatPercent() {
		return vatPercent
    }

    void setVatPercent(double vatPercent) {
		this.vatPercent = vatPercent
    }

    BigDecimal getTotalVAT() {
		return totalVAT
    }

    void setTotalVAT(BigDecimal totalVAT) {
		this.totalVAT = totalVAT
    }

}
