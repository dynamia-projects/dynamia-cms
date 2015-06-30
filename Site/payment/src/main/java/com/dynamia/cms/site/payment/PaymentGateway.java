package com.dynamia.cms.site.payment;

import java.util.Map;

import com.dynamia.cms.site.payment.domain.PaymentTransaction;

public interface PaymentGateway {

	String getName();

	String getId();

	String[] getRequiredParams();

	String[] getResponseParams();

	PaymentTransaction newTransaction(String source);

	public PaymentForm createForm(PaymentTransaction tx);

	public boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type);

	public abstract String getTransactionLocator();

}
