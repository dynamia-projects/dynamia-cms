package toosl.dynamia.cms.site.shoppingcart.dto

class ShoppingOrderDTO implements Serializable {

	private String site
    private Date timeStamp
    private String number
    private String shippingComments
    private String user
    private String userExternalRef
    private String userIdentification

    private String customer
    private String customerExternalRef
    private String customerIdentification

    private int quantity
    private BigDecimal subtotal
    private BigDecimal totalShipmentPrice
    private BigDecimal totalTaxes
    private BigDecimal totalPrice
    private BigDecimal totalUnit
    private float shipmentPercent

    private BigDecimal totalDiscount
    private String paymentUuid
    private String paymentStatus
    private String paymentMethod
    private BigDecimal paymentAmount
    private BigDecimal paymentTaxes
    private BigDecimal paymentTaxesBase
    private String paymentResponseCode

    private String shippingAddress
    private String shippingCity
    private String shippingCountry
    private String shippingPhone
    private String shippingEmail

    private String billingAddress
    private String billingCity
    private String billingCountry
    private String billingPhone
    private String billingEmail

    private boolean pickupAtStore
    private boolean payAtDelivery
    private boolean payLater

    private String userComments
    private String externalRef

    private List<ShoppingOrderItemDTO> items = new ArrayList<>()
    private Map<String, String> params = new HashMap<>()

    Map<String, String> getParams() {
		return params
    }

    void setParams(Map<String, String> params) {
		this.params = params
    }

    boolean isPayLater() {
		return payLater
    }

    void setPayLater(boolean payLater) {
		this.payLater = payLater
    }

    String getUserIdentification() {
		return userIdentification
    }

    void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification
    }

    String getCustomerIdentification() {
		return customerIdentification
    }

    void setCustomerIdentification(String customerIdentification) {
		this.customerIdentification = customerIdentification
    }

    Date getTimeStamp() {
		return timeStamp
    }

    void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp
    }

    String getSite() {
		return site
    }

    void setSite(String site) {
		this.site = site
    }

    String getNumber() {
		return number
    }

    void setNumber(String number) {
		this.number = number
    }

    String getShippingComments() {
		return shippingComments
    }

    void setShippingComments(String shippingComments) {
		this.shippingComments = shippingComments
    }

    String getUser() {
		return user
    }

    void setUser(String user) {
		this.user = user
    }

    String getCustomer() {
		return customer
    }

    void setCustomer(String customer) {
		this.customer = customer
    }

    String getUserExternalRef() {
		return userExternalRef
    }

    void setUserExternalRef(String userExternalRef) {
		this.userExternalRef = userExternalRef
    }

    String getCustomerExternalRef() {
		return customerExternalRef
    }

    void setCustomerExternalRef(String customerExternalRef) {
		this.customerExternalRef = customerExternalRef
    }

    int getQuantity() {
		return quantity
    }

    void setQuantity(int quantity) {
		this.quantity = quantity
    }

    BigDecimal getSubtotal() {
		return subtotal
    }

    void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal
    }

    BigDecimal getTotalShipmentPrice() {
		return totalShipmentPrice
    }

    void setTotalShipmentPrice(BigDecimal totalShipmentPrice) {
		this.totalShipmentPrice = totalShipmentPrice
    }

    BigDecimal getTotalTaxes() {
		return totalTaxes
    }

    void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes
    }

    BigDecimal getTotalPrice() {
		return totalPrice
    }

    void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice
    }

    BigDecimal getTotalUnit() {
		return totalUnit
    }

    void setTotalUnit(BigDecimal totalUnit) {
		this.totalUnit = totalUnit
    }

    float getShipmentPercent() {
		return shipmentPercent
    }

    void setShipmentPercent(float shipmentPercent) {
		this.shipmentPercent = shipmentPercent
    }

    BigDecimal getTotalDiscount() {
		return totalDiscount
    }

    void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount
    }

    String getPaymentUuid() {
		return paymentUuid
    }

    void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid
    }

    String getPaymentStatus() {
		return paymentStatus
    }

    void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus
    }

    String getPaymentMethod() {
		return paymentMethod
    }

    void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod
    }

    BigDecimal getPaymentAmount() {
		return paymentAmount
    }

    void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount
    }

    BigDecimal getPaymentTaxes() {
		return paymentTaxes
    }

    void setPaymentTaxes(BigDecimal paymentTaxes) {
		this.paymentTaxes = paymentTaxes
    }

    BigDecimal getPaymentTaxesBase() {
		return paymentTaxesBase
    }

    void setPaymentTaxesBase(BigDecimal paymentTaxesBase) {
		this.paymentTaxesBase = paymentTaxesBase
    }

    String getPaymentResponseCode() {
		return paymentResponseCode
    }

    void setPaymentResponseCode(String paymentResponseCode) {
		this.paymentResponseCode = paymentResponseCode
    }

    String getShippingAddress() {
		return shippingAddress
    }

    void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress
    }

    String getShippingCity() {
		return shippingCity
    }

    void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity
    }

    String getShippingCountry() {
		return shippingCountry
    }

    void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry
    }

    String getShippingPhone() {
		return shippingPhone
    }

    void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone
    }

    String getShippingEmail() {
		return shippingEmail
    }

    void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail
    }

    String getBillingAddress() {
		return billingAddress
    }

    void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress
    }

    String getBillingCity() {
		return billingCity
    }

    void setBillingCity(String billingCity) {
		this.billingCity = billingCity
    }

    String getBillingCountry() {
		return billingCountry
    }

    void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry
    }

    String getBillingPhone() {
		return billingPhone
    }

    void setBillingPhone(String billingPhone) {
		this.billingPhone = billingPhone
    }

    String getBillingEmail() {
		return billingEmail
    }

    void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail
    }

    boolean isPickupAtStore() {
		return pickupAtStore
    }

    void setPickupAtStore(boolean pickupAtStore) {
		this.pickupAtStore = pickupAtStore
    }

    boolean isPayAtDelivery() {
		return payAtDelivery
    }

    void setPayAtDelivery(boolean payAtDelivery) {
		this.payAtDelivery = payAtDelivery
    }

    String getUserComments() {
		return userComments
    }

    void setUserComments(String userComments) {
		this.userComments = userComments
    }

    String getExternalRef() {
		return externalRef
    }

    void setExternalRef(String externalRef) {
		this.externalRef = externalRef
    }

    List<ShoppingOrderItemDTO> getItems() {
		return items
    }

    void setItems(List<ShoppingOrderItemDTO> items) {
		this.items = items
    }

    void addItem(ShoppingOrderItemDTO item) {
		items.add(item)
    }

	@Override
    String toString() {
		return number
    }
}
