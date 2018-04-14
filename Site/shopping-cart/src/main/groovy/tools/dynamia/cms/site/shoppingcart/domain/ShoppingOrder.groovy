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

import java.util.Date

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotNull

import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Region
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.domain.UserContactInfo
import tools.dynamia.domain.BaseEntity
import tools.dynamia.domain.contraints.NotEmpty
import tools.dynamia.domain.util.ContactInfo
import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderDTO

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
        return transaction != null && transaction.getStatus() == PaymentTransactionStatus.COMPLETED
    }

    void sync() {
        if (shoppingCart != null && transaction != null) {
            shoppingCart.compute()
            syncTransaction()
        }

    }

    void syncTransaction() {
        if (shoppingCart != null && transaction != null) {
            transaction.setDocument(getNumber())
            transaction.setAmount(shoppingCart.getTotalPrice())
            transaction.setTaxes(shoppingCart.getTotalTaxes())
            if (transaction.getTaxes() != null && transaction.getTaxes().longValue() > 0) {
                transaction.setTaxesBase(shoppingCart.getSubtotal())
            }

            if (getShippingAddress() != null) {
                transaction.setShippingAddress(getShippingAddress().getInfo().getAddress())
                transaction.setShippingCity(getShippingAddress().getInfo().getCity())

            }
        }
    }

    ShoppingOrderDTO toDTO() {
        ShoppingOrderDTO dto = new ShoppingOrderDTO()
        dto.setNumber(number)
        dto.setShippingComments(shippingComments)
        dto.setPayAtDelivery(payAtDelivery)
        dto.setPickupAtStore(pickupAtStore)
        dto.setPayLater(payLater)
        dto.setSite(site.getKey())
        dto.setUserComments(userComments)
        dto.setExternalRef(externalRef)

        if (shoppingCart != null) {
            if (shoppingCart.getCustomer() != null) {
                User customer = shoppingCart.getCustomer()
                dto.setCustomer(customer.getFullName())
                dto.setCustomerExternalRef(customer.getExternalRef())
                dto.setCustomerIdentification(customer.getIdentification())
            }

            if (shoppingCart.getUser() != null) {
                User user = shoppingCart.getUser()
                dto.setUser(user.getFullName())
                dto.setUserExternalRef(user.getExternalRef())
                dto.setUserIdentification(user.getIdentification())
            }

            dto.setTimeStamp(shoppingCart.getTimeStamp())
            dto.setQuantity(shoppingCart.getQuantity())
            dto.setSubtotal(shoppingCart.getSubtotal())
            dto.setTotalShipmentPrice(shoppingCart.getTotalShipmentPrice())
            dto.setTotalTaxes(shoppingCart.getTotalTaxes())
            dto.setTotalPrice(shoppingCart.getTotalPrice())
            dto.setTotalUnit(shoppingCart.getTotalUnit())
            dto.setShipmentPercent(shoppingCart.getShipmentPercent())
            dto.setTotalDiscount(shoppingCart.getTotalDiscount())

            for (ShoppingCartItem item : shoppingCart.getItems()) {
                dto.addItem(item.toDTO())
            }
        }

        if (transaction != null) {
            dto.setPaymentAmount(transaction.getAmount())
            dto.setPaymentMethod(transaction.getPaymentMethod())
            dto.setPaymentResponseCode(transaction.getResponseCode())
            dto.setPaymentStatus(transaction.getStatusText())
            dto.setPaymentTaxes(transaction.getTaxes())
            dto.setPaymentTaxesBase(transaction.getTaxesBase())
            dto.setPaymentUuid(transaction.getUuid())
        }

        if (billingAddress != null) {
            ContactInfo ba = billingAddress.getInfo()
            dto.setBillingAddress(ba.getAddress())
            dto.setBillingCity(ba.getCity())
            dto.setBillingCountry(ba.getCountry())
            dto.setBillingEmail(ba.getEmail())
            dto.setBillingPhone(ba.getPhoneNumber())
        }

        if (shippingAddress != null) {
            ContactInfo sa = shippingAddress.getInfo()
            dto.setShippingAddress(sa.getAddress())
            dto.setShippingCity(sa.getCity())
            dto.setShippingCountry(sa.getCountry())
            dto.setShippingEmail(sa.getEmail())
            dto.setShippingPhone(sa.getPhoneNumber())
        }

        return dto
    }

    void addItem(ShoppingCartItem item, int qty) {
    }

    void checkRegionTaxes() {
        if (shippingAddress != null && shippingAddress.getCity() != null && shippingAddress.getCity().getRegion() != null) {
            Region region = shippingAddress.getCity().getRegion()
            if (region.getTaxPercent() > 0) {
                shoppingCart.setTaxName(region.getTaxName())
                shoppingCart.setTaxPercent(region.getTaxPercent())
                for (ShoppingCartItem item : shoppingCart.getItems()) {
                    item.setTaxName(region.getTaxName())
                    item.setTaxPercent(region.getTaxPercent())
                }
                shoppingCart.compute()
            }
        }
    }
}
