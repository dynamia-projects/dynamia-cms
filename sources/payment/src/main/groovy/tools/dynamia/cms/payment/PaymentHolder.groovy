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

package tools.dynamia.cms.payment

import org.springframework.context.annotation.Scope
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Component

@Component
@Scope("session")
class PaymentHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6257694168919195864L

    static PaymentHolder get() {
		return Containers.get().findObject(PaymentHolder.class)
    }

	private PaymentForm currentPaymentForm
    private tools.dynamia.cms.payment.domain.PaymentTransaction currentPaymentTransaction
    private tools.dynamia.cms.payment.domain.ManualPayment currentManualPayment

    PaymentForm getCurrentPaymentForm() {
		return currentPaymentForm
    }

    void setCurrentPaymentForm(PaymentForm currentPaymentForm) {
		this.currentPaymentForm = currentPaymentForm
    }

    tools.dynamia.cms.payment.domain.PaymentTransaction getCurrentPaymentTransaction() {
		return currentPaymentTransaction
    }

    void setCurrentPaymentTransaction(tools.dynamia.cms.payment.domain.PaymentTransaction currentPaymentTransaction) {
		this.currentPaymentTransaction = currentPaymentTransaction
    }

    tools.dynamia.cms.payment.domain.ManualPayment getCurrentManualPayment() {
		return currentManualPayment
    }

    void setCurrentManualPayment(tools.dynamia.cms.payment.domain.ManualPayment currentManualPayment) {
		this.currentManualPayment = currentManualPayment
    }

}
