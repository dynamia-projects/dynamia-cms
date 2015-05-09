package com.dynamia.cms.site.payment;

public interface PaymentTransactionListener {
	
	public void onStatusChanged(PaymentTransactionEvent evt);

}
