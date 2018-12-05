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
