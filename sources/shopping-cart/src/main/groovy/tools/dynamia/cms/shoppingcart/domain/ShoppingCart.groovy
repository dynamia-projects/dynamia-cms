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

import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.shoppingcart.domain.enums.ShoppingCartStatus
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
