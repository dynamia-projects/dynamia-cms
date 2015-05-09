package com.dynamia.cms.site.payment;

import java.util.Map;

import com.dynamia.cms.site.payment.domain.PaymentTransaction;

public interface PaymentGateway {

	String getName();

	String getId();

	String[] getRequiredParams();

	PaymentTransaction newTransaction(String source);

	public PaymentForm createForm(PaymentTransaction tx);

	public boolean commit(PaymentTransaction tx, Map<String, String> response);

	public abstract String getTransactionLocator();

}
