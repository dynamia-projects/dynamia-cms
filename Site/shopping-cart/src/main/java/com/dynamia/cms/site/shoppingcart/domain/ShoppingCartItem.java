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
package com.dynamia.cms.site.shoppingcart.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import tools.dynamia.domain.SimpleEntity;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "sc_shopping_carts_items")
public class ShoppingCartItem extends SimpleEntity {

    @ManyToOne
    @NotNull
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
    private String imageURL;
    private String imageName;
    private String URL;

    private String reference;
    private String sku;
    private Long refId;
    private String refClass;
    private BigDecimal discount;
    private String discountName;

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
            totalPrice = unitPrice.add(getDiscount()).multiply(new BigDecimal(quantity));
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
