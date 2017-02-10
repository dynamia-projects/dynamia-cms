package tools.dynamia.cms.site.payment.api;

public class PaymentSenderException extends RuntimeException {

	private String errorCode;

	public PaymentSenderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PaymentSenderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PaymentSenderException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public PaymentSenderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PaymentSenderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

}
