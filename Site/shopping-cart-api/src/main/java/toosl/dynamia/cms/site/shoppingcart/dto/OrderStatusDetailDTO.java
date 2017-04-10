package toosl.dynamia.cms.site.shoppingcart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderStatusDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8443711900361650187L;
	private String code;
	private String description;
	private double quantity;
	private BigDecimal price;
	private BigDecimal total;
	private Date shipDate;
	private String currency;
	private BigDecimal priceVAT;
	private BigDecimal vat;
	private double vatPercent;
	private BigDecimal totalVAT;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getPriceVAT() {
		return priceVAT;
	}

	public void setPriceVAT(BigDecimal priceVAT) {
		this.priceVAT = priceVAT;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public double getVatPercent() {
		return vatPercent;
	}

	public void setVatPercent(double vatPercent) {
		this.vatPercent = vatPercent;
	}

	public BigDecimal getTotalVAT() {
		return totalVAT;
	}

	public void setTotalVAT(BigDecimal totalVAT) {
		this.totalVAT = totalVAT;
	}

}
