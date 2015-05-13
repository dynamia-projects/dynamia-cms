/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart.services.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteParameter;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.PaymentTransactionEvent;
import com.dynamia.cms.site.payment.PaymentTransactionListener;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.cms.site.shoppingcart.ShoppingCartItemProvider;
import com.dynamia.cms.site.shoppingcart.ShoppingException;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCartItem;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.domains.enums.ShoppingCartStatus;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.commons.SimpleTemplateEngine;
import com.dynamia.tools.domain.ValidationError;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.util.DomainUtils;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Service;

/**
 *
 * @author mario_2
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
			config = new ShoppingSiteConfig();
			config.setSite(site);
			config.setDefaultCurrency(NumberFormat.getCurrencyInstance().getCurrency().getCurrencyCode());
			config.setMinPaymentAmount(BigDecimal.ZERO);

			PaymentGateway gateway = paymentService.getDefaultGateway();
			if (gateway != null) {
				config.setPaymentGatewayId(gateway.getId());
				config.setPaymentGatewayName(gateway.getName());
			}
			config = crudService.create(config);
		}

		return config;

	}

	@Override
	public ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config) {
		shoppingCart.compute();
		if (shoppingCart.getTotalPrice().longValue() < config.getMinPaymentAmount().longValue()) {
			throw new ValidationError("Minimo valor de venta es $" + config.getMinPaymentAmount());
		}

		PaymentGateway gateway = paymentService.findGateway(config.getPaymentGatewayId());
		PaymentTransaction tx = gateway.newTransaction(config.getSite().getKey());
		tx.setGatewayId(gateway.getId());
		tx.setAmount(shoppingCart.getTotalPrice());
		tx.setTaxes(shoppingCart.getTotalTaxes());
		if (tx.getTaxes() == null || tx.getTaxes().longValue() == 0) {
			tx.setTaxesBase(BigDecimal.ZERO);
		} else {
			tx.setTaxesBase(shoppingCart.getSubtotal());
		}
		tx.setCurrency(config.getDefaultCurrency());

		tx.setEmail(UserHolder.get().getCurrent().getUsername());

		ShoppingOrder order = new ShoppingOrder();
		order.setShoppingCart(shoppingCart);
		order.setTransaction(tx);
		order.setSite(config.getSite());

		return order;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrder(ShoppingOrder order) {
		if (order.getId() == null) {
			SiteParameter param = siteService.getSiteParameter(order.getSite(), LAST_SHOPPING_ORDER_NUMBER, "0");
			long lastNumber = Long.parseLong(param.getValue());
			lastNumber++;

			param.setValue(String.valueOf(lastNumber));
			crudService.save(param);

			String number = DomainUtils.formatNumberWithZeroes(lastNumber, 10000);
			order.setNumber(number);
			order.getShoppingCart().setName(number);
			order.getShoppingCart().setStatus(ShoppingCartStatus.COMPLETED);

			order.getTransaction().setDescription(
					"Orden No. " + order.getNumber() + ". Compra de " + order.getShoppingCart().getQuantity() + " producto(s)");

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

			order.getTransaction().setStatus(PaymentTransactionStatus.CANCELLED);
			order.getShoppingCart().setStatus(ShoppingCartStatus.CANCELLED);
			crudService.update(order);
		}
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
			}
		}

	}

	private void notifyOrderCompleted(ShoppingOrder order) {
		// TODO: Async mode
		// TODO: Generate PDF
		// TODO: Notify user
		// TODO: Notify store

	}
}
