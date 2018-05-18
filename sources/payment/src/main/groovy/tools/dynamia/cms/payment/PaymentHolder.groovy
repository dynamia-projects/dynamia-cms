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
