/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package toosl.dynamia.cms.shoppingcart.api

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
