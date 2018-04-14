package toosl.dynamia.cms.site.shoppingcart.api;

public class ShoppingOrderSenderException extends RuntimeException {

	private String errorCode;

	public ShoppingOrderSenderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ShoppingOrderSenderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ShoppingOrderSenderException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ShoppingOrderSenderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ShoppingOrderSenderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

}
