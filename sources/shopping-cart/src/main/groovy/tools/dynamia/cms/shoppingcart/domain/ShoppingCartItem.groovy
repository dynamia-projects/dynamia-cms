/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.shoppingcart.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.commons.BeanUtils
import tools.dynamia.commons.BigDecimalUtils
import tools.dynamia.domain.SimpleEntity
import toosl.dynamia.cms.shoppingcart.dto.ShoppingOrderItemDTO

import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "sc_shopping_carts_items")
class ShoppingCartItem extends SimpleEntity {

    /**
     *
     */
    static final long serialVersionUID = -883947947386952432L
    @ManyToOne
    @NotNull
    @JsonIgnore
    ShoppingCart shoppingCart
    String code
    String name
    @Column(length = 1000)
    String description
    @Min(1L)
    int quantity = 1
    BigDecimal taxes = BigDecimal.ZERO
    @Min(0L)
    @NotNull
    BigDecimal unitPrice = BigDecimal.ZERO
    @Min(0L)
    @NotNull
    BigDecimal totalPrice = BigDecimal.ZERO
    BigDecimal shipmentPrice = BigDecimal.ZERO
    BigDecimal subtotal = BigDecimal.ZERO
    String imageURL
    String imageName
    String URL
    String brandName
    String categoryName
    String reference
    String sku
    Long refId
    @JsonIgnore
    String refClass
    BigDecimal discount
    String discountName
    boolean editable = true

    @Transient
    @JsonIgnore
    List<ShoppingCartItem> children = new ArrayList<>()
    @Transient
    @JsonIgnore
    ShoppingCartItem parent

    String taxName
    double taxPercent
    boolean taxable
    boolean taxIncluded
    String unit


    void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.longValue() < 0) {
            unitPrice = BigDecimal.ONE
        }
        this.unitPrice = unitPrice
    }


    void compute() {
        if (discount == null) {
            discount = BigDecimal.ZERO
        }

        if (quantity > 0 && unitPrice != null && unitPrice.doubleValue() > 0) {
            subtotal = (unitPrice + discount) * quantity
        }

        if (taxable && taxPercent > 0) {
            taxes = BigDecimalUtils.computePercent(unitPrice, taxPercent, taxIncluded) * quantity
        }

        if (shoppingCart.shipmentPercent > 0) {
            shipmentPrice = BigDecimalUtils.computePercent(subtotal, shoppingCart.shipmentPercent, false)
        } else {
            shipmentPrice = BigDecimal.ZERO
        }

        if (shipmentPrice == null) {
            shipmentPrice = BigDecimal.ZERO
        }

        if (taxes == null) {
            taxes = BigDecimal.ZERO
        }

        totalPrice = subtotal.add(taxes).add(shipmentPrice)
    }

    ShoppingCartItem clone() {
        ShoppingCartItem clone = BeanUtils.clone(this, "id")
        clone.id = null
        clone.code = code
        clone.description = description
        clone.imageName = imageName
        clone.imageURL = imageURL
        clone.name = name
        clone.quantity = quantity
        clone.refClass = refClass
        clone.refId = refId
        clone.shipmentPrice = shipmentPrice
        clone.taxes = taxes
        clone.totalPrice = totalPrice
        clone.unitPrice = unitPrice
        clone.URL = URL
        clone.sku = sku
        clone.taxable = taxable
        clone.taxName = taxName
        clone.taxPercent = taxPercent
        clone.taxIncluded = taxIncluded
        return clone
    }

    ShoppingOrderItemDTO toDTO() {
        ShoppingOrderItemDTO dto = new ShoppingOrderItemDTO()
        BeanUtils.setupBean(dto, this)
        dto.code = code
        dto.description = description
        dto.imageName = imageName
        dto.imageURL = imageURL
        dto.name = name
        dto.quantity = quantity
        dto.refClass = refClass
        dto.refId = refId
        dto.shipmentPrice = shipmentPrice
        dto.taxes = taxes
        dto.totalPrice = totalPrice
        dto.unitPrice = unitPrice
        dto.setURL(URL)
        dto.sku = sku
        return dto
    }

}
