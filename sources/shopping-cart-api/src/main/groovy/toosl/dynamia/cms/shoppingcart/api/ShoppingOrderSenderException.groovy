package toosl.dynamia.cms.shoppingcart.api

class ShoppingOrderSenderException extends RuntimeException {

	private String errorCode

    ShoppingOrderSenderException() {
		super()
        // TODO Auto-generated constructor stub
	}

    ShoppingOrderSenderException(String message, Throwable cause) {
		super(message, cause)
        // TODO Auto-generated constructor stub
	}

    ShoppingOrderSenderException(String message, String errorCode) {
		super(message)
        this.errorCode = errorCode
    }

    ShoppingOrderSenderException(String message) {
		super(message)
        // TODO Auto-generated constructor stub
	}

    ShoppingOrderSenderException(Throwable cause) {
		super(cause)
        // TODO Auto-generated constructor stub
	}

    String getErrorCode() {
		return errorCode
    }

}
