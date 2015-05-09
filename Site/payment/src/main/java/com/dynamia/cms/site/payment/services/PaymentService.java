package com.dynamia.cms.site.payment.services;

import java.util.Map;

import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;

public interface PaymentService {

	public abstract Map<String, String> getGatewayConfigMap(PaymentGateway gateway, String source);

	public abstract PaymentGatewayConfig getConfig(PaymentGateway gateway, String name, String source);

	public abstract void addGatewayConfig(PaymentGatewayConfig config);

	public abstract PaymentGateway findGateway(String gatewayId);

	public abstract PaymentTransaction findTransaction(PaymentGateway gateway, Map<String, String> response);

	public abstract PaymentGateway getDefaultGateway();

}