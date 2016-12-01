package toosl.dynamia.cms.site.shoppingcart.api;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3203433648732109207L;
	private boolean error;
	private String errorCode;
	private String errorMessage;
	private String content;
	private String source;

	private Response() {

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getSource() {
		return source;
	}

	public boolean isError() {
		return error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "Response [error=" + error + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", content="
				+ content + ", source=" + source + "]";
	}

	public static Response buildError(String source, String code, String message) {
		Response response = new Response();
		response.source = source;
		response.error = true;
		response.errorCode = code;
		response.errorMessage = message;
		return response;
	}

	public static Response build(String source, String content) {
		Response response = new Response();
		response.source = source;
		response.content = content;
		return response;

	}

	public static Response find(List<Response> response, String source) {
		Optional<Response> result = response.stream().filter(r -> r.getSource().equals(source)).findFirst();
		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

}
