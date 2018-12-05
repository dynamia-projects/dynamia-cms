/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.payment.api

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
        Optional<Response> result = response.stream().filter { r -> r.source == source }.findFirst()
        if (result.present) {
            return result.get()
        } else {
            return null
        }
    }

}
