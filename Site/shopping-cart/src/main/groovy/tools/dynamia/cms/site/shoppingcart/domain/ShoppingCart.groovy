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

import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.shoppingcart.domain.enums.ShoppingCartStatus
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "sc_shopping_carts")
class ShoppingCart extends SimpleEntity implements SiteAware {

    String name
    String title
    @Temporal(TemporalType.TIMESTAMP)
    Date timeStamp = new Date()
    @OneToOne
    User user
    @OneToOne
    User customer
    @OneToOne
    @NotNull
    Site site
    int quantity
    BigDecimal subtotal
    BigDecimal totalShipmentPrice
    BigDecimal totalTaxes
    BigDecimal totalPrice
    BigDecimal totalUnit
    float shipmentPercent
    String taxName
    Double taxPercent

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    List<ShoppingCartItem> items = new ArrayList<>()

    @Enumerated(EnumType.ORDINAL)
    ShoppingCartStatus status = ShoppingCartStatus.NEW

    BigDecimal totalDiscount


    ShoppingCart() {
        // TODO Auto-generated constructor stub
    }

    ShoppingCart(String name) {
        super()
        this.name = name
    }

    void addItem(ShoppingCartItem item, int qty) {
        ShoppingCartItem addedItem = getItemByCode(item.getCode())
        if (addedItem != null) {
            addedItem.setQuantity(addedItem.getQuantity() + qty)
        } else {
            item.setQuantity(qty)
            items.add(item)
            item.setShoppingCart(this)
        }
        compute()
    }

    void addItem(ShoppingCartItem item) {
        addItem(item, 1)
    }

    boolean removeItem(ShoppingCartItem item) {
        ShoppingCartItem addedItem = getItemByCode(item.getCode())
        if (addedItem != null) {
            addedItem.setShoppingCart(null)
            items.remove(addedItem)
            compute()
            return true
        }
        return false
    }

    boolean removeItem(String code) {
        ShoppingCartItem addedItem = getItemByCode(code)
        if (addedItem != null) {
            addedItem.setShoppingCart(null)
            items.remove(addedItem)
            if (addedItem.getChildren() != null && !addedItem.getChildren().isEmpty()) {
                addedItem.getChildren().forEach { c -> items.remove(c) }
            }
            compute()
            return true
        }
        return false
    }

    void compute() {
        totalPrice = BigDecimal.ZERO
        totalTaxes = BigDecimal.ZERO
        totalShipmentPrice = BigDecimal.ZERO
        totalDiscount = BigDecimal.ZERO
        totalUnit = BigDecimal.ZERO
        subtotal = BigDecimal.ZERO
        quantity = 0

        for (ShoppingCartItem item : items) {
            item.compute()
            quantity += item.getQuantity()
            totalTaxes = totalTaxes.add(item.getTaxes())
            totalShipmentPrice = totalShipmentPrice.add(item.getShipmentPrice())
            totalDiscount = totalDiscount.add(item.getDiscount())
            totalUnit = totalUnit.add(item.getUnitPrice())
            subtotal = subtotal.add(item.getSubtotal())
        }


        computeTotalOnly()
    }

    void computeTotalOnly() {
        totalPrice = subtotal.add(totalTaxes).add(totalShipmentPrice)
    }

    ShoppingCartItem getItemByCode(String code) {
        for (ShoppingCartItem item : items) {
            if (item.getCode() == code && item.isEditable()) {
                return item
            }
        }
        return null
    }

    boolean isEmpty() {
        return items.isEmpty()
    }

    User getTargetUser() {
        if (customer != null) {
            return customer
        } else {
            return user
        }
    }

}