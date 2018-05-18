package toosl.dynamia.cms.shoppingcart.dto

class ShoppingOrderItemDTO implements Serializable {
	private String code
    private String name

    private String description

    private int quantity = 1
    private BigDecimal taxes = BigDecimal.ZERO

    private BigDecimal unitPrice = BigDecimal.ZERO

    private BigDecimal totalPrice = BigDecimal.ZERO
    private BigDecimal shipmentPrice = BigDecimal.ZERO
    private String imageURL
    private String imageName
    private String URL

    private String reference
    private String sku
    private Long refId
    private String refClass
    private BigDecimal discount
    private String discountName
    private String taxName
    private double taxPercent
    private boolean taxable
    private boolean taxIncluded
    private String unit

    String getTaxName() {
		return taxName
    }

    void setTaxName(String taxName) {
		this.taxName = taxName
    }

    double getTaxPercent() {
		return taxPercent
    }

    void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent
    }

    boolean isTaxable() {
		return taxable
    }

    void setTaxable(boolean taxable) {
		this.taxable = taxable
    }

    boolean isTaxIncluded() {
		return taxIncluded
    }

    void setTaxIncluded(boolean taxIncluded) {
		this.taxIncluded = taxIncluded
    }

    String getUnit() {
		return unit
    }

    void setUnit(String unit) {
		this.unit = unit
    }

    String getCode() {
		return code
    }

    void setCode(String code) {
		this.code = code
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getDescription() {
		return description
    }

    void setDescription(String description) {
		this.description = description
    }

    int getQuantity() {
		return quantity
    }

    void setQuantity(int quantity) {
		this.quantity = quantity
    }

    BigDecimal getTaxes() {
		return taxes
    }

    void setTaxes(BigDecimal taxes) {
		this.taxes = taxes
    }

    BigDecimal getUnitPrice() {
		return unitPrice
    }

    void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice
    }

    BigDecimal getTotalPrice() {
		return totalPrice
    }

    void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice
    }

    BigDecimal getShipmentPrice() {
		return shipmentPrice
    }

    void setShipmentPrice(BigDecimal shipmentPrice) {
		this.shipmentPrice = shipmentPrice
    }

    String getImageURL() {
		return imageURL
    }

    void setImageURL(String imageURL) {
		this.imageURL = imageURL
    }

    String getImageName() {
		return imageName
    }

    void setImageName(String imageName) {
		this.imageName = imageName
    }

    String getURL() {
		return URL
    }

    void setURL(String uRL) {
		URL = uRL
    }

    String getReference() {
		return reference
    }

    void setReference(String reference) {
		this.reference = reference
    }

    String getSku() {
		return sku
    }

    void setSku(String sku) {
		this.sku = sku
    }

    Long getRefId() {
		return refId
    }

    void setRefId(Long refId) {
		this.refId = refId
    }

    String getRefClass() {
		return refClass
    }

    void setRefClass(String refClass) {
		this.refClass = refClass
    }

    BigDecimal getDiscount() {
		return discount
    }

    void setDiscount(BigDecimal discount) {
		this.discount = discount
    }

    String getDiscountName() {
		return discountName
    }

    void setDiscountName(String discountName) {
		this.discountName = discountName
    }

}
