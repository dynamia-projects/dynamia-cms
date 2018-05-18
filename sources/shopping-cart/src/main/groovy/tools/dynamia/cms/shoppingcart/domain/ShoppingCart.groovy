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
package tools.dynamia.cms.shoppingcart.domain

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.domain.User
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
    tools.dynamia.cms.shoppingcart.domain.enums.ShoppingCartStatus status = tools.dynamia.cms.shoppingcart.domain.enums.ShoppingCartStatus.NEW

    BigDecimal totalDiscount


    ShoppingCart() {
        // TODO Auto-generated constructor stub
    }

    ShoppingCart(String name) {
        super()
        this.name = name
    }

    void addItem(ShoppingCartItem item, int qty) {
        ShoppingCartItem addedItem = getItemByCode(item.code)
        if (addedItem != null) {
            addedItem.quantity = addedItem.quantity + qty
        } else {
            item.quantity = qty
            items.add(item)
            item.shoppingCart = this
        }
        compute()
    }

    void addItem(ShoppingCartItem item) {
        addItem(item, 1)
    }

    boolean removeItem(ShoppingCartItem item) {
        ShoppingCartItem addedItem = getItemByCode(item.code)
        if (addedItem != null) {
            addedItem.shoppingCart = null
            items.remove(addedItem)
            compute()
            return true
        }
        return false
    }

    boolean removeItem(String code) {
        ShoppingCartItem addedItem = getItemByCode(code)
        if (addedItem != null) {
            addedItem.shoppingCart = null
            items.remove(addedItem)
            if (addedItem.children != null && !addedItem.children.empty) {
                addedItem.children.forEach { c -> items.remove(c) }
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
            quantity += item.quantity
            totalTaxes = totalTaxes.add(item.taxes)
            totalShipmentPrice = totalShipmentPrice.add(item.shipmentPrice)
            totalDiscount = totalDiscount.add(item.discount)
            totalUnit = totalUnit.add(item.unitPrice)
            subtotal = subtotal.add(item.subtotal)
        }


        computeTotalOnly()
    }

    void computeTotalOnly() {
        totalPrice = subtotal.add(totalTaxes).add(totalShipmentPrice)
    }

    ShoppingCartItem getItemByCode(String code) {
        for (ShoppingCartItem item : items) {
            if (item.code == code && item.editable) {
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
