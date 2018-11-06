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
package tools.dynamia.cms.shoppingcart.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.HtmlTableBuilder
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.domain.SiteParameter
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailTemplate
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.PaymentTransactionEvent
import tools.dynamia.cms.payment.PaymentTransactionListener
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.ShoppingCartItemProvider
import tools.dynamia.cms.shoppingcart.ShoppingException
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.domain.enums.ShoppingCartStatus
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserContactInfo
import tools.dynamia.commons.ClassMessages
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.domain.util.DomainUtils
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.web.util.HttpRemotingServiceClient
import toosl.dynamia.cms.shoppingcart.api.Response
import toosl.dynamia.cms.shoppingcart.api.ShoppingOrderSender
import toosl.dynamia.cms.shoppingcart.api.ShoppingOrderSenderException
import toosl.dynamia.cms.shoppingcart.dto.ShoppingOrderDTO

/**
 * @author Mario Serrano Leones
 */
@Service
@CompileStatic
class ShoppingCartServiceImpl implements ShoppingCartService, PaymentTransactionListener {

    private static final String CACHE_NAME = "shopping"

    private static final String LAST_SHOPPING_ORDER_NUMBER = "lastShoppingOrderNumber"

    @Autowired
    private CrudService crudService

    @Autowired
    private PaymentService paymentService

    @Autowired
    private SiteService siteService

    @Autowired
    private MailService mailService

    private LoggingService logger = new SLF4JLoggingService(ShoppingCartService.class)

    private ClassMessages classMessages

    @Override
    ShoppingCartItem getItem(Site site, String code) {
        for (ShoppingCartItemProvider provider : Containers.get().findObjects(ShoppingCartItemProvider.class)) {
            ShoppingCartItem item = provider.getItem(site, code)
            if (item != null) {
                return item
            }
        }
        return null
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Cacheable(value = ShoppingCartServiceImpl.CACHE_NAME, key = "'cfg'+#site.key")
    ShoppingSiteConfig getConfiguration(Site site) {
        ShoppingSiteConfig config = crudService.findSingle(ShoppingSiteConfig.class, "site", site)
        if (config == null) {
            config = new ShoppingSiteConfig()
            config.site = site
            crudService.create(config)
        }

        return config

    }

    @Override
    ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config) {
        shoppingCart.shipmentPercent = config.shipmentPercent
        shoppingCart.compute()

        if (shoppingCart.totalPrice.longValue() < config.minPaymentAmount.longValue()) {
            throw new ValidationError("Minimo valor de venta es " + config.minPaymentAmount)
        }

        if (shoppingCart.totalPrice.longValue() > config.maxPaymentAmount.longValue()) {
            throw new ValidationError("El valor maximo de ventas en linea es " + config.maxPaymentAmount)
        }

        if (config.minQuantityByCart > 0 && shoppingCart.quantity < config.minQuantityByCart) {
            throw new ValidationError("You should buy at least " + config.minQuantityByCart + " products")
        }

        if (config.minQuantityByProducts > 0) {
            for (ShoppingCartItem item : (shoppingCart.items)) {
                if (item.quantity < config.minQuantityByProducts) {
                    throw new ValidationError("You should buy at least " + config.minQuantityByProducts + " " + item.name)
                }
            }
        }

        User user = UserHolder.get().current
        shoppingCart.user = user
        shoppingCart.customer = UserHolder.get().customer

        if (user != null && user.profile == UserProfile.SELLER && shoppingCart.customer == null) {
            throw new ValidationError("Seleccione cliente para crear orden de pedido")
        }

        PaymentTransaction tx = null

        if (config.paymentGatewayAccount != null) {
            PaymentGateway gateway = paymentService.findGateway(config.paymentGatewayAccount.gatewayId)
            tx = gateway.newTransaction(config.paymentGatewayAccount, CMSUtil.getSiteURL(config.site, "/"))
            tx.gatewayId = gateway.id
        } else {
            tx = newLocalPaymentTransaction()
        }

        tx.currency = config.defaultCurrency
        tx.email = user.username
        if (shoppingCart.customer != null) {
            User customer = shoppingCart.customer
            tx.payerFullname = customer.fullName
            tx.payerDocument = customer.identification
            tx.payerCode = customer.code
        } else {
            tx.payerFullname = user.fullName
            tx.payerDocument = user.identification
            tx.payerCode = user.code
        }

        ShoppingOrder order = new ShoppingOrder()
        order.shoppingCart = shoppingCart
        order.transaction = tx
        order.sync()
        order.site = config.site

        return order
    }

    private PaymentTransaction newLocalPaymentTransaction() {
        PaymentTransaction tx = new PaymentTransaction()
        tx.gatewayId = "local"
        tx.startDate = new Date()
        tx.status = PaymentTransactionStatus.COMPLETED
        tx.statusText = ""
        return tx
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveOrder(ShoppingOrder order) {
        if (order.id == null) {
            SiteParameter param = siteService.getSiteParameter(order.site, LAST_SHOPPING_ORDER_NUMBER, "0")
            ShoppingSiteConfig config = getConfiguration(order.site)

            long lastNumber = Long.parseLong(param.value)
            lastNumber++

            param.value = String.valueOf(lastNumber)
            crudService.save(param)

            String number = DomainUtils.formatNumberWithZeroes(lastNumber, 10000)
            order.number = number
            order.shoppingCart.name = number

            order.checkRegionTaxes()

            if (order.pickupAtStore || order.payAtDelivery) {
                order.shoppingCart.shipmentPercent = 0
                order.shoppingCart.compute()
            } else if (order.shoppingCart.totalShipmentPrice.longValue() < config.minShipmentAmount
                    .longValue()) {
                order.shoppingCart.totalShipmentPrice = config.minShipmentAmount
                order.shoppingCart.computeTotalOnly()
            }

            if (order.payLater) {
                order.transaction = newLocalPaymentTransaction()
                order.transaction.confirmed = true
            }

            order.syncTransaction()
            order.shoppingCart.status = ShoppingCartStatus.COMPLETED

            order.transaction.description = "Orden No. " + order.number + ". Compra de "
            +order.shoppingCart.quantity + " producto(s)"

            if (order.billingAddress != null) {
                order.transaction.payerPhoneNumber = order.billingAddress.info.phoneNumber
                order.transaction.payerMobileNumber = order.billingAddress.info.mobileNumber
            }

            crudService.save(order)
        } else {
            crudService.update(order)
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void cancelOrder(ShoppingOrder order) {
        if (order.id != null) {

            if (order.transaction.status == PaymentTransactionStatus.COMPLETED) {
                throw new ShoppingException(
                        "No se puede cancelar order No. " + order.number + " porque ya fue COMPLETADA y PAGADA")
            }

            if (order.transaction.status == PaymentTransactionStatus.PROCESSING) {
                throw new ShoppingException(
                        "No se puede cancelar order No. " + order.number + " porque ya esta siendo PROCESADA")
            }

            if (order.transaction.status == PaymentTransactionStatus.REJECTED) {
                throw new ShoppingException(
                        "No se puede cancelar order No. " + order.number + " porque ya fue RECHAZADA")
            }

            order.transaction.status = PaymentTransactionStatus.CANCELLED
            order.shoppingCart.status = ShoppingCartStatus.CANCELLED
            crudService.update(order)

            recreateShoppingCart(order.shoppingCart)
        }
    }

    private void recreateShoppingCart(ShoppingCart shoppingCart) {
        ShoppingCart newsc = ShoppingCartHolder.get().getCart("shop")
        for (ShoppingCartItem oldItem : (shoppingCart.items)) {
            ShoppingCartItem newItem = oldItem.clone()
            newsc.addItem(newItem, newItem.quantity)
        }

        newsc.compute()

    }

    private Map<String, Object> getDescriptionsVars(ShoppingCart shoppingCart) {
        Map<String, Object> vars = new HashMap<String, Object>()
        vars.put("QUANTITY", shoppingCart.quantity)
        return vars
    }

    @Override
    void onStatusChanged(PaymentTransactionEvent evt) {
        PaymentTransaction tx = evt.transaction
        if (tx.status == PaymentTransactionStatus.COMPLETED) {
            ShoppingOrder order = crudService.findSingle(ShoppingOrder.class, "transaction", tx)
                if (order != null) {
                notifyOrderCompleted(order)
            } else {
                logger.error("No shopping order found for transaction " + tx.uuid)
            }
        }

    }

    @Override
    void notifyOrderCompleted(ShoppingOrder order) {

        ShoppingSiteConfig config = getConfiguration(order.site)
        logger.info("Order Completed " + order.number)

        notifyOrderCustomer(config, order)
        notifyOrderInternal(config, order)

    }

    private void notifyOrderInternal(ShoppingSiteConfig config, ShoppingOrder order) {
        try {
            if (config.notificationMailTemplate != null) {
                logger.info("Sending notification email " + config.notificationEmails)
                MailMessage notificationMessage = createMailMessage(config.notificationMailTemplate,
                        config.mailAccount, order)

                if (config.notificationEmails.contains(",")) {
                    String[] emails = config.notificationEmails.split(",")
                    for (int i = 0; i < emails.length; i++) {
                        String otherEmail = emails[i]
                        notificationMessage.addTo(otherEmail.trim())
                    }
                } else {
                    notificationMessage.to = config.notificationEmails
                }

                mailService.sendAsync(notificationMessage)
                logger.info("Notification email queued")
            }
        } catch (Exception e) {
            logger.error("Error sending notification email for order " + order.number, e)
            e.printStackTrace()
        }
    }

    private void notifyOrderCustomer(ShoppingSiteConfig config, ShoppingOrder order) {
        try {
            if (config.orderCompletedMailTemplate != null) {
                logger.info("Sending customer email " + order.shoppingCart.user.username)
                MailMessage customerMessage = createMailMessage(config.orderCompletedMailTemplate,
                        config.mailAccount, order)
                User user = order.shoppingCart.user
                customerMessage.to = user.username
                if (user.contactInfo != null && user.contactInfo.email != null
                        && !user.contactInfo.email.empty
                        && !user.contactInfo.email.equals(user.username)) {
                    customerMessage.addTo(user.username)
                    customerMessage.addTo(user.contactInfo.email)
                }

                mailService.sendAsync(customerMessage)
                logger.info("Customer email queued")
            } else {
                logger.error("No email template found for customer Orden Completed notification")
            }
        } catch (Exception e) {
            logger.error("Error sending customer email for order " + order.number, e)
            e.printStackTrace()

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void shipOrder(ShoppingOrder shoppingOrder) {

        if (!shoppingOrder.completed || !shoppingOrder.transaction.confirmed) {
            throw new ValidationError(msg("OrderNotConfirmed", shoppingOrder.number))
        }

        if (shoppingOrder.shipped) {
            throw new ValidationError(msg("OrderAlreadyShipped", shoppingOrder.number))
        }

        if (shoppingOrder.shippingCompany == null) {
            throw new ValidationError(msg("SelectShippingCompany"))
        }

        if (shoppingOrder.trackingNumber == null || shoppingOrder.trackingNumber.empty) {
            throw new ValidationError(msg("EnterTrackingNumber"))
        }

        if (shoppingOrder.estimatedArrivalDate == null) {
            throw new ValidationError(msg("SelectEstimatedArrivalDate"))
        }

        if (shoppingOrder.invoiceNumber == null || shoppingOrder.invoiceNumber.empty) {
            throw new ValidationError(msg("EnterInvoiceNumber"))
        }

        shoppingOrder.shipped = true
        shoppingOrder.shippingDate = new Date()
        crudService.save(shoppingOrder)
        notifyOrderShipped(shoppingOrder)

    }

    @Override
    void notifyOrderShipped(ShoppingOrder order) {
        if (!order.shipped) {
            throw new ValidationError("Order " + order.number + " is not shipped cannot be notify it")
        }

        ShoppingSiteConfig config = getConfiguration(order.site)
        logger.info("Order Shipped " + order.number)
        try {
            if (config.orderShippedMailTemplate != null) {
                logger.info("Sending customer email " + order.shoppingCart.user.username)
                MailMessage customerMessage = createMailMessage(config.orderShippedMailTemplate,
                        config.mailAccount, order)
                User user = order.shoppingCart.user
                customerMessage.to = user.username
                if (user.contactInfo.email != null && !user.contactInfo.email.empty
                        && !user.contactInfo.email.equals(user.username)) {
                    customerMessage.addTo(user.contactInfo.email)
                }

                mailService.send(customerMessage)
                logger.info("Customer email Sended")
            }
        } catch (Exception e) {
            logger.error("Error sending customer email for order " + order.number, e)
            e.printStackTrace()
        }
    }

    private MailMessage createMailMessage(MailTemplate template, MailAccount mailAccount, ShoppingOrder order) {
        MailMessage message = new MailMessage()
        message.mailAccount = mailAccount
        message.template = template
        message.templateModel.put("order", order)

        ShoppingCart cart = crudService.reload(order.shoppingCart)

        message.templateModel["cart"] = cart
        message.templateModel["itemsTable"] = buildShoppingCartTable(cart)
        message.templateModel["items"] = cart.items
        message.templateModel["shippingAddress"] = getShippingAddress(order)
        message.templateModel["billingAddress"] = order.billingAddress
        message.templateModel["tx"] = order.transaction
        message.templateModel["user"] = order.shoppingCart.user
        message.templateModel["identification"] = order.shoppingCart.user.identification
        message.templateModel["deliveryType"] = order.payAtDelivery ? "Pago Envio Contraentrega" : ""
        return message
    }

    private Object getShippingAddress(ShoppingOrder order) {
        UserContactInfo address = order.shippingAddress
        if (order.pickupAtStore) {
            address = new UserContactInfo()
            address.name = "Recoger en tienda"
            address.info.address = "Se le informara via email la tienda donde recoger su mercancia"
            address.info.country = ""
            address.info.city = ""
            address.info.region = ""
            address.info.phoneNumber = ""
            address.info.mobileNumber = ""
        }

        return address
    }

    private String buildShoppingCartTable(ShoppingCart shoppingCart) {
        HtmlTableBuilder htb = new HtmlTableBuilder()

        htb.addColumnHeader("")

        htb.addColumnHeader(msg("ItemSKU"), msg("ItemName"), msg("ItemUnitPrice"), msg("ItemQuantity"),
                msg("ItemPrice"))
        for (ShoppingCartItem item : (shoppingCart.items)) {
            htb.addRow()
            htb.addData(htb.rowCount, item.sku, item.name, item.unitPrice, item.quantity,
                    item.totalPrice)
        }
        htb.addRow()
        htb.addData("", "", "<b>SUBTOTAL</b>", "", shoppingCart.quantity, shoppingCart.subtotal)
        htb.addRow()
        htb.addData("", "", "<b>ENVIO</b>", "", "", shoppingCart.totalShipmentPrice)
        htb.addRow()
        htb.addData("", "", "<b>TOTAL</b>", "", "", shoppingCart.totalPrice)
        return htb.render()
    }

    private String msg(String key, Object... params) {
        if (classMessages == null) {
            this.classMessages = ClassMessages.get(ShoppingCartService.class)
        }

        return classMessages.get(key, params)
    }

    @Override
    void sendOrder(ShoppingOrder order) {

        order = crudService.reload(order)
        ShoppingSiteConfig cfg = getConfiguration(order.site)

        if (cfg.orderSenderURL == null || cfg.orderSenderURL.empty) {
            throw new ValidationError("Cannot send order " + order.number + " because no sender is configured")
        }

        ShoppingOrderSender sender = HttpRemotingServiceClient.build(ShoppingOrderSender.class)
                .setServiceURL(cfg.orderSenderURL)
                .getProxy()

        ShoppingOrderDTO dto = order.toDTO()
        Response response = sender.sendOrder(dto, cfg.parametersAsMap)

        if (response != null) {
            if (response.error) {
                order.errorCode = response.errorCode
                order.errorMessage = response.errorMessage
            } else {
                order.errorCode = null
                order.errorMessage = null
                order.externalRef = response.content
                order.sended = true
            }
        }

    }

    @Override
    List<ShoppingOrder> getOrders(User user) {
        return crudService.find(ShoppingOrder.class,
                QueryParameters
                        .with("transaction.status",
                        QueryConditions.in(PaymentTransactionStatus.COMPLETED,
                                PaymentTransactionStatus.PROCESSING))
                        .add("shoppingCart.user", user).orderBy("id", false))
    }

    @Override
    void sendAllOrders() {
        logger.info("Sending all shopping orders")
        try {
            List<Site> sites = crudService.find(Site.class, "offline", false)
            for (Site site : sites) {
                ShoppingSiteConfig cfg = getConfiguration(site)
                if (cfg != null && cfg.autoSendOrders && cfg.orderSenderURL != null
                        && !cfg.orderSenderURL.empty) {
                    ShoppingOrderSender sender = HttpRemotingServiceClient.build(ShoppingOrderSender.class)
                            .setServiceURL(cfg.orderSenderURL)
                            .getProxy()


                    crudService.executeWithinTransaction {

                        List<ShoppingOrder> orders = crudService.find(ShoppingOrder.class,
                                QueryParameters.with("sended", false)
                                        .add("transaction.status", PaymentTransactionStatus.COMPLETED)
                                        .add("site", site))
                        logger.info("Sending " + orders.size() + " orders for site " + site.name)

                        sendOrders(sender, orders, cfg)
                    }

                }
                logger.info("Shopping Orders Sended")
            }
        } catch (Exception e) {
            logger.error("Error sending orders", e)

        }
    }

    private void sendOrders(ShoppingOrderSender sender, List<ShoppingOrder> orders, ShoppingSiteConfig cfg) {
        List<ShoppingOrderDTO> dtos = orders.collect { it.toDTO() }

        if (!dtos.empty) {
            logger.info("Calling Sender " + sender)
            try {
                List<Response> response = sender.sendOrders(dtos, cfg.parametersAsMap)

                if (response != null) {
                    logger.info("Sending response recieved. " + response.size())
                    for (ShoppingOrder order : orders) {
                        Response resp = Response.find(response, order.number)
                        if (resp != null) {
                            if (resp.error) {
                                order.errorCode = resp.errorCode
                                order.errorMessage = resp.errorMessage
                            } else {
                                order.errorCode = null
                                order.errorMessage = null
                                order.externalRef = resp.content
                                order.sended = true
                            }
                        }
                        crudService.update(order)
                        logger.info("==> Updating order " + order.number + " ==> " + resp)
                    }
                }
            } catch (ShoppingOrderSenderException e) {
                logger.error("Error", e)
            }
        }
    }

}
