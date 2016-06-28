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
package tools.dynamia.cms.site.shoppingcart.services.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.core.HtmlTableBuilder;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.domain.SiteParameter;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.domain.MailTemplate;
import tools.dynamia.cms.site.mail.services.MailService;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.PaymentTransactionEvent;
import tools.dynamia.cms.site.payment.PaymentTransactionListener;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.cms.site.shoppingcart.ShoppingCartHolder;
import tools.dynamia.cms.site.shoppingcart.ShoppingCartItemProvider;
import tools.dynamia.cms.site.shoppingcart.ShoppingException;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import tools.dynamia.cms.site.shoppingcart.domain.enums.ShoppingCartStatus;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.domain.UserContactInfo;

import tools.dynamia.commons.ClassMessages;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.DomainUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Service;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
class ShoppingCartServiceImpl implements ShoppingCartService, PaymentTransactionListener {

    private static final String LAST_SHOPPING_ORDER_NUMBER = "lastShoppingOrderNumber";

    @Autowired
    private CrudService crudService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private MailService mailService;

    private LoggingService logger = new SLF4JLoggingService(ShoppingCartService.class);

    private ClassMessages classMessages;

    @Override
    public ShoppingCartItem getItem(Site site, String code) {
        for (ShoppingCartItemProvider provider : Containers.get().findObjects(ShoppingCartItemProvider.class)) {
            ShoppingCartItem item = provider.getItem(site, code);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    @Override
    public ShoppingSiteConfig getConfiguration(Site site) {
        ShoppingSiteConfig config = crudService.findSingle(ShoppingSiteConfig.class, "site", site);
        if (config == null) {
            throw new ShoppingException("No shopping site configuration found");
        }

        return config;

    }

    @Override
    public ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config) {
        shoppingCart.setShipmentPercent(config.getShipmentPercent());
        shoppingCart.compute();

        if (shoppingCart.getTotalPrice().longValue() < config.getMinPaymentAmount().longValue()) {
            throw new ValidationError("Minimo valor de venta es $" + config.getMinPaymentAmount());
        }

        if (shoppingCart.getTotalPrice().longValue() > config.getMaxPaymentAmount().longValue()) {
            throw new ValidationError("El valor maximo de ventas en linea es $" + config.getMaxPaymentAmount());
        }

        PaymentGateway gateway = paymentService.findGateway(config.getPaymentGatewayId());
        PaymentTransaction tx = gateway.newTransaction(config.getSite().getKey());
        tx.setGatewayId(gateway.getId());
        tx.setCurrency(config.getDefaultCurrency());

        User user = UserHolder.get().getCurrent();
        tx.setEmail(user.getUsername());
        tx.setPayerFullname(user.getFullName());
        tx.setPayerDocument(user.getIdentification());
        shoppingCart.setUser(user);

        ShoppingOrder order = new ShoppingOrder();
        order.setShoppingCart(shoppingCart);
        order.setTransaction(tx);
        order.sync();
        order.setSite(config.getSite());

        return order;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrder(ShoppingOrder order) {
        if (order.getId() == null) {
            SiteParameter param = siteService.getSiteParameter(order.getSite(), LAST_SHOPPING_ORDER_NUMBER, "0");
            ShoppingSiteConfig config = getConfiguration(order.getSite());

            long lastNumber = Long.parseLong(param.getValue());
            lastNumber++;

            param.setValue(String.valueOf(lastNumber));
            crudService.save(param);

            String number = DomainUtils.formatNumberWithZeroes(lastNumber, 10000);
            order.setNumber(number);
            order.getShoppingCart().setName(number);
            if (order.isPickupAtStore() || order.isPayAtDelivery()) {
                order.getShoppingCart().setShipmentPercent(0);
                order.getShoppingCart().compute();
            } else if (order.getShoppingCart().getTotalShipmentPrice().longValue() < config.getMinShipmentAmount().longValue()) {
                order.getShoppingCart().setTotalShipmentPrice(config.getMinShipmentAmount());
                order.getShoppingCart().computeTotalOnly();
            }

            order.syncTransaction();
            order.getShoppingCart().setStatus(ShoppingCartStatus.COMPLETED);

            order.getTransaction().setDescription(
                    "Orden No. " + order.getNumber() + ". Compra de " + order.getShoppingCart().getQuantity() + " producto(s)");

            order.getTransaction().setPayerPhoneNumber(order.getBillingAddress().getInfo().getPhoneNumber());
            order.getTransaction().setPayerMobileNumber(order.getBillingAddress().getInfo().getMobileNumber());

            crudService.create(order);
        } else {
            crudService.update(order);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelOrder(ShoppingOrder order) {
        if (order.getId() != null) {

            if (order.getTransaction().getStatus() == PaymentTransactionStatus.COMPLETED) {
                throw new ShoppingException("No se puede cancelar order No. " + order.getNumber() + " porque ya fue COMPLETADA y PAGADA");
            }

            if (order.getTransaction().getStatus() == PaymentTransactionStatus.PROCESSING) {
                throw new ShoppingException("No se puede cancelar order No. " + order.getNumber() + " porque ya esta siendo PROCESADA");
            }

            if (order.getTransaction().getStatus() == PaymentTransactionStatus.REJECTED) {
                throw new ShoppingException("No se puede cancelar order No. " + order.getNumber() + " porque ya fue RECHAZADA");
            }

            order.getTransaction().setStatus(PaymentTransactionStatus.CANCELLED);
            order.getShoppingCart().setStatus(ShoppingCartStatus.CANCELLED);
            crudService.update(order);

            recreateShoppingCart(order.getShoppingCart());
        }
    }

    private void recreateShoppingCart(ShoppingCart shoppingCart) {
        ShoppingCart newsc = ShoppingCartHolder.get().getCart("shop");
        for (ShoppingCartItem oldItem : shoppingCart.getItems()) {
            ShoppingCartItem newItem = oldItem.clone();
            newsc.addItem(newItem);
        }

        newsc.compute();

    }

    private Map<String, Object> getDescriptionsVars(ShoppingCart shoppingCart) {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("QUANTITY", shoppingCart.getQuantity());
        return vars;
    }

    @Override
    public void onStatusChanged(PaymentTransactionEvent evt) {
        PaymentTransaction tx = evt.getTransaction();
        if (tx.getStatus() == PaymentTransactionStatus.COMPLETED) {
            ShoppingOrder order = crudService.findSingle(ShoppingOrder.class, "transaction", tx);
            if (order != null) {
                notifyOrderCompleted(order);
            } else {
                logger.error("No shopping order found for transaction " + tx.getUuid());
            }
        }

    }

    @Override
    public void notifyOrderCompleted(ShoppingOrder order) {
        ShoppingSiteConfig config = getConfiguration(order.getSite());
        logger.info("Order Completed " + order.getNumber());
        notifyOrderCustomer(config, order);
        notifyOrderInternal(config, order);
    }

    private void notifyOrderInternal(ShoppingSiteConfig config, ShoppingOrder order) {
        try {
            if (config.getNotificationMailTemplate() != null) {
                logger.info("Sending notification email " + config.getNotificationEmails());
                MailMessage notificationMessage = createMailMessage(config.getNotificationMailTemplate(), config.getMailAccount(), order);

                if (config.getNotificationEmails().contains(",")) {
                    String[] emails = config.getNotificationEmails().split(",");
                    for (int i = 0; i < emails.length; i++) {
                        String otherEmail = emails[i];
                        notificationMessage.addTo(otherEmail.trim());
                    }
                } else {
                    notificationMessage.setTo(config.getNotificationEmails());
                }

                mailService.send(notificationMessage);
                logger.info("Notification email Sended");
            }
        } catch (Exception e) {
            logger.error("Error sending notification email for order " + order.getNumber(), e);
            e.printStackTrace();
        }
    }

    private void notifyOrderCustomer(ShoppingSiteConfig config, ShoppingOrder order) {
        try {
            if (config.getOrderCompletedMailTemplate() != null) {
                logger.info("Sending customer email " + order.getShoppingCart().getUser().getUsername());
                MailMessage customerMessage = createMailMessage(config.getOrderCompletedMailTemplate(), config.getMailAccount(), order);
                User user = order.getShoppingCart().getUser();
                customerMessage.setTo(user.getUsername());
                if (user.getContactInfo() != null
                        && user.getContactInfo().getEmail() != null
                        && !user.getContactInfo().getEmail().isEmpty()
                        && !user.getContactInfo().getEmail().equals(user.getUsername())) {
                    customerMessage.addTo(user.getUsername());
                    customerMessage.addTo(user.getContactInfo().getEmail());
                }

                mailService.send(customerMessage);
                logger.info("Customer email Sended");
            } else {
                logger.error("No email template found for customer Orden Completed notification");
            }
        } catch (Exception e) {
            logger.error("Error sending customer email for order " + order.getNumber(), e);
            e.printStackTrace();

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shipOrder(ShoppingOrder shoppingOrder) {

        if (!shoppingOrder.isCompleted() || !shoppingOrder.getTransaction().isConfirmed()) {
            throw new ValidationError(msg("OrderNotConfirmed", shoppingOrder.getNumber()));
        }

        if (shoppingOrder.isShipped()) {
            throw new ValidationError(msg("OrderAlreadyShipped", shoppingOrder.getNumber()));
        }

        if (shoppingOrder.getShippingCompany() == null) {
            throw new ValidationError(msg("SelectShippingCompany"));
        }

        if (shoppingOrder.getTrackingNumber() == null || shoppingOrder.getTrackingNumber().isEmpty()) {
            throw new ValidationError(msg("EnterTrackingNumber"));
        }

        if (shoppingOrder.getEstimatedArrivalDate() == null) {
            throw new ValidationError(msg("SelectEstimatedArrivalDate"));
        }

        if (shoppingOrder.getInvoiceNumber() == null || shoppingOrder.getInvoiceNumber().isEmpty()) {
            throw new ValidationError(msg("EnterInvoiceNumber"));
        }

        shoppingOrder.setShipped(true);
        shoppingOrder.setShippingDate(new Date());
        crudService.save(shoppingOrder);
        notifyOrderShipped(shoppingOrder);

    }

    @Override
    public void notifyOrderShipped(ShoppingOrder order) {
        if (!order.isShipped()) {
            throw new ValidationError("Order " + order.getNumber() + " is not shipped cannot be notify it");
        }

        ShoppingSiteConfig config = getConfiguration(order.getSite());
        logger.info("Order Shipped " + order.getNumber());
        try {
            if (config.getOrderShippedMailTemplate() != null) {
                logger.info("Sending customer email " + order.getShoppingCart().getUser().getUsername());
                MailMessage customerMessage = createMailMessage(config.getOrderShippedMailTemplate(), config.getMailAccount(), order);
                User user = order.getShoppingCart().getUser();
                customerMessage.setTo(user.getUsername());
                if (user.getContactInfo().getEmail() != null
                        && !user.getContactInfo().getEmail().isEmpty()
                        && !user.getContactInfo().getEmail().equals(user.getUsername())) {
                    customerMessage.addTo(user.getContactInfo().getEmail());
                }

                mailService.send(customerMessage);
                logger.info("Customer email Sended");
            }
        } catch (Exception e) {
            logger.error("Error sending customer email for order " + order.getNumber(), e);
            e.printStackTrace();
        }
    }

    private MailMessage createMailMessage(MailTemplate template, MailAccount mailAccount, ShoppingOrder order) {
        MailMessage message = new MailMessage();
        message.setMailAccount(mailAccount);
        message.setTemplate(template);
        message.getTemplateModel().put("order", order);

        ShoppingCart cart = crudService.reload(order.getShoppingCart());

        message.getTemplateModel().put("cart", cart);
        message.getTemplateModel().put("itemsTable", buildShoppingCartTable(cart));
        message.getTemplateModel().put("items", cart.getItems());
        message.getTemplateModel().put("shippingAddress", getShippingAddress(order));
        message.getTemplateModel().put("billingAddress", order.getBillingAddress());
        message.getTemplateModel().put("tx", order.getTransaction());
        message.getTemplateModel().put("user", order.getShoppingCart().getUser());
        message.getTemplateModel().put("identification", order.getShoppingCart().getUser().getIdentification());
        message.getTemplateModel().put("deliveryType", order.isPayAtDelivery() ? "Pago Envio Contraentrega" : "");
        return message;
    }

    private Object getShippingAddress(ShoppingOrder order) {
        UserContactInfo address = order.getShippingAddress();
        if (order.isPickupAtStore()) {
            address = new UserContactInfo();
            address.setName("Recoger en tienda");
            address.getInfo().setAddress("Se le informara via email la tienda donde recoger su mercancia");
            address.getInfo().setCountry("");
            address.getInfo().setCity("");
            address.getInfo().setRegion("");
            address.getInfo().setPhoneNumber("");
            address.getInfo().setMobileNumber("");
        }

        return address;
    }

    private String buildShoppingCartTable(ShoppingCart shoppingCart) {
        HtmlTableBuilder htb = new HtmlTableBuilder();

        htb.addColumnHeader("");

        htb.addColumnHeader(msg("ItemSKU"), msg("ItemName"), msg("ItemUnitPrice"), msg("ItemQuantity"), msg("ItemPrice"));
        for (ShoppingCartItem item : shoppingCart.getItems()) {
            htb.addRow();
            htb.addData(htb.getRowCount(), item.getSku(), item.getName(), item.getUnitPrice(), item.getQuantity(), item.getTotalPrice());
        }
        htb.addRow();
        htb.addData("", "", "<b>SUBTOTAL</b>", "", shoppingCart.getQuantity(), shoppingCart.getSubtotal());
        htb.addRow();
        htb.addData("", "", "<b>ENVIO</b>", "", "", shoppingCart.getTotalShipmentPrice());
        htb.addRow();
        htb.addData("", "", "<b>TOTAL</b>", "", "", shoppingCart.getTotalPrice());
        return htb.render();
    }

    private String msg(String key, Object... params) {
        if (classMessages == null) {
            this.classMessages = ClassMessages.get(ShoppingCartService.class);
        }

        return classMessages.get(key, params);
    }
}
