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
package tools.dynamia.cms.site.shoppingcart.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.commons.BeanUtils
import tools.dynamia.commons.BigDecimalUtils
import tools.dynamia.domain.SimpleEntity
import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderItemDTO

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
        if (quantity > 0 && unitPrice != null && unitPrice.doubleValue() > 0) {
            subtotal = unitPrice.add(getDiscount()).multiply(new BigDecimal(quantity))
        }

        if (taxable && taxPercent > 0) {
            taxes = BigDecimalUtils.computePercent(unitPrice, taxPercent, taxIncluded)
                    .multiply(new BigDecimal(quantity))
        }

        if (shoppingCart.getShipmentPercent() > 0) {
            shipmentPrice = BigDecimalUtils.computePercent(subtotal, shoppingCart.getShipmentPercent(), false)
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
        clone.setId(null)
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
        dto.setCode(code)
        dto.setDescription(description)
        dto.setImageName(imageName)
        dto.setImageURL(imageURL)
        dto.setName(name)
        dto.setQuantity(quantity)
        dto.setRefClass(refClass)
        dto.setRefId(refId)
        dto.setShipmentPrice(shipmentPrice)
        dto.setTaxes(taxes)
        dto.setTotalPrice(totalPrice)
        dto.setUnitPrice(unitPrice)
        dto.setURL(URL)
        dto.setSku(sku)
        return dto
    }

}
