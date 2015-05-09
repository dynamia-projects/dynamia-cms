package com.dynamia.cms.site.payment;

import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;

public class PaymentTransactionEvent {

	private PaymentTransaction transaction;
	private PaymentTransactionStatus oldStatus;

	public PaymentTransactionEvent(PaymentTransaction transaction, PaymentTransactionStatus oldStatus) {
		super();
		this.transaction = transaction;
		this.oldStatus = oldStatus;
	}

	public PaymentTransaction getTransaction() {
		return transaction;
	}

	public PaymentTransactionStatus getOldStatus() {
		return oldStatus;
	}

}
