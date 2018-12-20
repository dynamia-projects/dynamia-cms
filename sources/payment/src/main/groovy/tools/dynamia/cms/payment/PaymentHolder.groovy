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
