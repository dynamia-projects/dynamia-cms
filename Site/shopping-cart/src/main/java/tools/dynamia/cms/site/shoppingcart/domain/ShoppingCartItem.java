/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.shoppingcart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.BigDecimalUtils;
import tools.dynamia.domain.SimpleEntity;
import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderItemDTO;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "sc_shopping_carts_items")
public class ShoppingCartItem extends SimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -883947947386952432L;
	@ManyToOne
	@NotNull
	@JsonIgnore
	private ShoppingCart shoppingCart;
	private String code;
	private String name;
	@Column(length = 1000)
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
	private BigDecimal subtotal = BigDecimal.ZERO;
	private String imageURL;
	private String imageName;
	private String URL;
	private String brandName;
	private String categoryName;
	private String reference;
	private String sku;
	private Long refId;
	@JsonIgnore
	private String refClass;
	private BigDecimal discount;
	private String discountName;
	private boolean editable = true;

	@Transient
	@JsonIgnore
	private List<ShoppingCartItem> children = new ArrayList<>();
	@Transient
	@JsonIgnore
	private ShoppingCartItem parent;

	private String taxName;
	private double taxPercent;
	private boolean taxable;
	private boolean taxIncluded;
	private String unit;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(boolean taxable) {
		this.taxable = taxable;
	}

	public boolean isTaxIncluded() {
		return taxIncluded;
	}

	public void setTaxIncluded(boolean taxIncluded) {
		this.taxIncluded = taxIncluded;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<ShoppingCartItem> getChildren() {
		return children;
	}

	public void setChildren(List<ShoppingCartItem> children) {
		this.children = children;
	}

	public ShoppingCartItem getParent() {
		return parent;
	}

	public void setParent(ShoppingCartItem parent) {
		this.parent = parent;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public BigDecimal getDiscount() {
		if (discount == null) {
			discount = BigDecimal.ZERO;
		}
		if (discount.longValue() > 0) {
			discount = discount.negate();
		}
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

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
		if (unitPrice == null || unitPrice.longValue() < 0) {
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
		if (quantity > 0 && unitPrice != null && unitPrice.doubleValue() > 0) {
			subtotal = unitPrice.add(getDiscount()).multiply(new BigDecimal(quantity));
		}

		if (taxable && taxPercent > 0) {
			taxes = BigDecimalUtils.computePercent(unitPrice, taxPercent, taxIncluded)
					.multiply(new BigDecimal(quantity));
		}

		if (shoppingCart.getShipmentPercent() > 0) {
			shipmentPrice = BigDecimalUtils.computePercent(subtotal, shoppingCart.getShipmentPercent(), false);

		}

		if (shipmentPrice == null) {
			shipmentPrice = BigDecimal.ZERO;
		}

		if (taxes == null) {
			taxes = BigDecimal.ZERO;
		}

		totalPrice = subtotal.add(taxes).add(shipmentPrice);
	}

	public ShoppingCartItem clone() {
		ShoppingCartItem clone = BeanUtils.clone(this, "id");
		clone.setId(null);
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
		clone.sku = sku;
		return clone;
	}

	public ShoppingOrderItemDTO toDTO() {
		ShoppingOrderItemDTO dto = new ShoppingOrderItemDTO();
		BeanUtils.setupBean(dto, this);
		dto.setCode(code);
		dto.setDescription(description);
		dto.setImageName(imageName);
		dto.setImageURL(imageURL);
		dto.setName(name);
		dto.setQuantity(quantity);
		dto.setRefClass(refClass);
		dto.setRefId(refId);
		dto.setShipmentPrice(shipmentPrice);
		dto.setTaxes(taxes);
		dto.setTotalPrice(totalPrice);
		dto.setUnitPrice(unitPrice);
		dto.setURL(URL);
		dto.setSku(sku);
		return dto;
	}

}
