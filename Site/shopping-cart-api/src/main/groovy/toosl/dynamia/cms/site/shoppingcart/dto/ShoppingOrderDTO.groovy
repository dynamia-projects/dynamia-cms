package toosl.dynamia.cms.site.shoppingcart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingOrderDTO implements Serializable {

	private String site;
	private Date timeStamp;
	private String number;
	private String shippingComments;
	private String user;
	private String userExternalRef;
	private String userIdentification;

	private String customer;
	private String customerExternalRef;
	private String customerIdentification;

	private int quantity;
	private BigDecimal subtotal;
	private BigDecimal totalShipmentPrice;
	private BigDecimal totalTaxes;
	private BigDecimal totalPrice;
	private BigDecimal totalUnit;
	private float shipmentPercent;

	private BigDecimal totalDiscount;
	private String paymentUuid;
	private String paymentStatus;
	private String paymentMethod;
	private BigDecimal paymentAmount;
	private BigDecimal paymentTaxes;
	private BigDecimal paymentTaxesBase;
	private String paymentResponseCode;

	private String shippingAddress;
	private String shippingCity;
	private String shippingCountry;
	private String shippingPhone;
	private String shippingEmail;

	private String billingAddress;
	private String billingCity;
	private String billingCountry;
	private String billingPhone;
	private String billingEmail;

	private boolean pickupAtStore;
	private boolean payAtDelivery;
	private boolean payLater;

	private String userComments;
	private String externalRef;

	private List<ShoppingOrderItemDTO> items = new ArrayList<>();
	private Map<String, String> params = new HashMap<>();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isPayLater() {
		return payLater;
	}

	public void setPayLater(boolean payLater) {
		this.payLater = payLater;
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public String getCustomerIdentification() {
		return customerIdentification;
	}

	public void setCustomerIdentification(String customerIdentification) {
		this.customerIdentification = customerIdentification;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getShippingComments() {
		return shippingComments;
	}

	public void setShippingComments(String shippingComments) {
		this.shippingComments = shippingComments;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getUserExternalRef() {
		return userExternalRef;
	}

	public void setUserExternalRef(String userExternalRef) {
		this.userExternalRef = userExternalRef;
	}

	public String getCustomerExternalRef() {
		return customerExternalRef;
	}

	public void setCustomerExternalRef(String customerExternalRef) {
		this.customerExternalRef = customerExternalRef;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotalShipmentPrice() {
		return totalShipmentPrice;
	}

	public void setTotalShipmentPrice(BigDecimal totalShipmentPrice) {
		this.totalShipmentPrice = totalShipmentPrice;
	}

	public BigDecimal getTotalTaxes() {
		return totalTaxes;
	}

	public void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalUnit() {
		return totalUnit;
	}

	public void setTotalUnit(BigDecimal totalUnit) {
		this.totalUnit = totalUnit;
	}

	public float getShipmentPercent() {
		return shipmentPercent;
	}

	public void setShipmentPercent(float shipmentPercent) {
		this.shipmentPercent = shipmentPercent;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public String getPaymentUuid() {
		return paymentUuid;
	}

	public void setPaymentUuid(String paymentUuid) {
		this.paymentUuid = paymentUuid;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getPaymentTaxes() {
		return paymentTaxes;
	}

	public void setPaymentTaxes(BigDecimal paymentTaxes) {
		this.paymentTaxes = paymentTaxes;
	}

	public BigDecimal getPaymentTaxesBase() {
		return paymentTaxesBase;
	}

	public void setPaymentTaxesBase(BigDecimal paymentTaxesBase) {
		this.paymentTaxesBase = paymentTaxesBase;
	}

	public String getPaymentResponseCode() {
		return paymentResponseCode;
	}

	public void setPaymentResponseCode(String paymentResponseCode) {
		this.paymentResponseCode = paymentResponseCode;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public String getShippingEmail() {
		return shippingEmail;
	}

	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingPhone() {
		return billingPhone;
	}

	public void setBillingPhone(String billingPhone) {
		this.billingPhone = billingPhone;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	public boolean isPickupAtStore() {
		return pickupAtStore;
	}

	public void setPickupAtStore(boolean pickupAtStore) {
		this.pickupAtStore = pickupAtStore;
	}

	public boolean isPayAtDelivery() {
		return payAtDelivery;
	}

	public void setPayAtDelivery(boolean payAtDelivery) {
		this.payAtDelivery = payAtDelivery;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public List<ShoppingOrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ShoppingOrderItemDTO> items) {
		this.items = items;
	}

	public void addItem(ShoppingOrderItemDTO item) {
		items.add(item);
	}

	@Override
	public String toString() {
		return getNumber();
	}
}
