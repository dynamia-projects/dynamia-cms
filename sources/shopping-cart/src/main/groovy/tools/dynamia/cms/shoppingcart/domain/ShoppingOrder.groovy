/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
import tools.dynamia.cms.core.domain.Region
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.domain.BaseEntity
import tools.dynamia.domain.contraints.NotEmpty
import tools.dynamia.domain.util.ContactInfo
import toosl.dynamia.cms.shoppingcart.dto.ShoppingOrderDTO

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "sc_orders", uniqueConstraints = [@UniqueConstraint(columnNames = ["site_id", "number"])])
class ShoppingOrder extends BaseEntity implements SiteAware {

    @OneToOne
    @NotNull
    Site site

    @NotNull
    @NotEmpty
    String number

    String invoiceNumber
    String invoiceId
    String trackingNumber
    @OneToOne
    ShippingCompany shippingCompany
    @Temporal(TemporalType.DATE)
    Date estimatedArrivalDate
    String shippingComments
    @Temporal(TemporalType.TIMESTAMP)
    Date shippingDate
    boolean shipped

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    ShoppingCart shoppingCart

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    PaymentTransaction transaction

    @OneToOne
    UserContactInfo shippingAddress

    @OneToOne
    UserContactInfo billingAddress

    boolean pickupAtStore
    boolean payAtDelivery
    boolean payLater

    @Column(length = 5000)
    String userComments
    String externalRef
    boolean sended
    @Column(length = 5000)
    String errorMessage
    String errorCode

    
    boolean isCompleted() {
        return transaction != null && transaction.status == PaymentTransactionStatus.COMPLETED
    }

    void sync() {
        if (shoppingCart != null && transaction != null) {
            shoppingCart.compute()
            syncTransaction()
        }

    }

    void syncTransaction() {
        if (shoppingCart != null && transaction != null) {
            transaction.document = number
            transaction.amount = shoppingCart.totalPrice
            transaction.taxes = shoppingCart.totalTaxes
            if (transaction.taxes != null && transaction.taxes.longValue() > 0) {
                transaction.taxesBase = shoppingCart.subtotal
            }

            if (shippingAddress != null) {
                transaction.shippingAddress = shippingAddress.info.address
                transaction.shippingCity = shippingAddress.info.city

            }
        }
    }

    ShoppingOrderDTO toDTO() {
        ShoppingOrderDTO dto = new ShoppingOrderDTO()
        dto.number = number
        dto.shippingComments = shippingComments
        dto.payAtDelivery = payAtDelivery
        dto.pickupAtStore = pickupAtStore
        dto.payLater = payLater
        dto.site = site.key
        dto.userComments = userComments
        dto.externalRef = externalRef

        if (shoppingCart != null) {
            if (shoppingCart.customer != null) {
                User customer = shoppingCart.customer
                dto.customer = customer.fullName
                dto.customerExternalRef = customer.externalRef
                dto.customerIdentification = customer.identification
            }

            if (shoppingCart.user != null) {
                User user = shoppingCart.user
                dto.user = user.fullName
                dto.userExternalRef = user.externalRef
                dto.userIdentification = user.identification
            }

            dto.timeStamp = shoppingCart.timeStamp
            dto.quantity = shoppingCart.quantity
            dto.subtotal = shoppingCart.subtotal
            dto.totalShipmentPrice = shoppingCart.totalShipmentPrice
            dto.totalTaxes = shoppingCart.totalTaxes
            dto.totalPrice = shoppingCart.totalPrice
            dto.totalUnit = shoppingCart.totalUnit
            dto.shipmentPercent = shoppingCart.shipmentPercent
            dto.totalDiscount = shoppingCart.totalDiscount

            for (ShoppingCartItem item : (shoppingCart.items)) {
                dto.addItem(item.toDTO())
            }
        }

        if (transaction != null) {
            dto.paymentAmount = transaction.amount
            dto.paymentMethod = transaction.paymentMethod
            dto.paymentResponseCode = transaction.responseCode
            dto.paymentStatus = transaction.statusText
            dto.paymentTaxes = transaction.taxes
            dto.paymentTaxesBase = transaction.taxesBase
            dto.paymentUuid = transaction.uuid
        }

        if (billingAddress != null) {
            ContactInfo ba = billingAddress.info
            dto.billingAddress = ba.address
            dto.billingCity = ba.city
            dto.billingCountry = ba.country
            dto.billingEmail = ba.email
            dto.billingPhone = ba.phoneNumber
        }

        if (shippingAddress != null) {
            ContactInfo sa = shippingAddress.info
            dto.shippingAddress = sa.address
            dto.shippingCity = sa.city
            dto.shippingCountry = sa.country
            dto.shippingEmail = sa.email
            dto.shippingPhone = sa.phoneNumber
        }

        return dto
    }

    void addItem(ShoppingCartItem item, int qty) {
    }

    void checkRegionTaxes() {
        if (shippingAddress != null && shippingAddress.city != null && shippingAddress.city.region != null) {
            Region region = shippingAddress.city.region
            if (region.taxPercent > 0) {
                shoppingCart.taxName = region.taxName
                shoppingCart.taxPercent = region.taxPercent
                for (ShoppingCartItem item : (shoppingCart.items)) {
                    item.taxName = region.taxName
                    item.taxPercent = region.taxPercent
                }
                shoppingCart.compute()
            }
        }
    }
}
