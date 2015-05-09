package com.dynamia.cms.site.payment;

public class PaymentException extends RuntimeException {

	public PaymentException() {
		super();

	}

	public PaymentException(String message, Throwable cause) {
		super(message, cause);

	}

	public PaymentException(String message) {
		super(message);

	}

	public PaymentException(Throwable cause) {
		super(cause);

	}

}
