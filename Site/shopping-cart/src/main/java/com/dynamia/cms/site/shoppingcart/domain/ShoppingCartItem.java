/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.domain;

import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.ValidationError;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author mario_2
 */
@Entity
@Table(name = "sc_shopping_carts_items")
public class ShoppingCartItem extends SimpleEntity {

	@ManyToOne
	@NotNull
	private ShoppingCart shoppingCart;
	private String code;
	private String name;
	@Column(length=1000)
	private String description;
	@Min(value = 1)
	private int quantity = 1;
	private BigDecimal taxes = BigDecimal.ZERO;
	@Min(value = 0)
	@NotNull
	private BigDecimal unitPrice = BigDecimal.ZERO;
	@Min(value = 0)
	@NotNull
	private BigDecimal totalPrice = BigDecimal.ZERO;
	private BigDecimal shipmentPrice = BigDecimal.ZERO;
	private String imageURL;
	private String imageName;
	private String URL;

	private String reference;
	private String sku;
	private Long refId;
	private String refClass;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRefClass() {
		return refClass;
	}

	public void setRefClass(String refClass) {
		this.refClass = refClass;
	}

	public BigDecimal getShipmentPrice() {
		return shipmentPrice;
	}

	public void setShipmentPrice(BigDecimal shipmentPrice) {
		this.shipmentPrice = shipmentPrice;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity <= 0) {
			quantity = 1;
		}
		this.quantity = quantity;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		if (unitPrice == null || unitPrice.longValue() <= 0) {
			unitPrice = BigDecimal.ONE;
		}
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	void compute() {
		if (quantity > 0 && unitPrice != null && unitPrice.longValue() > 0) {
			totalPrice = unitPrice.multiply(new BigDecimal(quantity));
		}
	}

	public ShoppingCartItem clone() {
		ShoppingCartItem clone = new ShoppingCartItem();
		clone.code = code;
		clone.description = description;
		clone.imageName = imageName;
		clone.imageURL = imageURL;
		clone.name = name;
		clone.quantity = quantity;
		clone.refClass = refClass;
		clone.refId = refId;
		clone.shipmentPrice = shipmentPrice;
		clone.taxes = taxes;
		clone.totalPrice = totalPrice;
		clone.unitPrice = unitPrice;
		clone.URL = URL;

		return clone;
	}

}
