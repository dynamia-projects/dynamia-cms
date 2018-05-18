package tools.dynamia.cms.payment.api

class PaymentSenderException extends RuntimeException {

	private String errorCode

    PaymentSenderException() {
		super()
        // TODO Auto-generated constructor stub
	}

    PaymentSenderException(String message, Throwable cause) {
		super(message, cause)
        // TODO Auto-generated constructor stub
	}

    PaymentSenderException(String message, String errorCode) {
		super(message)
        this.errorCode = errorCode
    }

    PaymentSenderException(String message) {
		super(message)
        // TODO Auto-generated constructor stub
	}

    PaymentSenderException(Throwable cause) {
		super(cause)
        // TODO Auto-generated constructor stub
	}

    String getErrorCode() {
		return errorCode
    }

}
