package tools.dynamia.cms.site.payment.api

import java.io.Serializable
import java.util.List
import java.util.Optional

class Response implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3203433648732109207L
    private boolean error
    private String errorCode
    private String errorMessage
    private String content
    private String source

    private Response() {

    }

    String getErrorMessage() {
        return errorMessage
    }

    String getSource() {
        return source
    }

    boolean isError() {
        return error
    }

    String getErrorCode() {
        return errorCode
    }

    String getContent() {
        return content
    }

    @Override
    String toString() {
        return "Response [error=" + error + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", content="
        +content + ", source=" + source + "]"
    }

    static Response buildError(String source, String code, String message) {
        Response response = new Response()
        response.source = source
        response.error = true
        response.errorCode = code
        response.errorMessage = message
        return response
    }

    static Response build(String source, String content) {
        Response response = new Response()
        response.source = source
        response.content = content
        return response

    }

    static Response find(List<Response> response, String source) {
        Optional<Response> result = response.stream().filter { r -> r.getSource() == source }.findFirst()
        if (result.isPresent()) {
            return result.get()
        } else {
            return null
        }
    }

}
