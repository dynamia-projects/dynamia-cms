package tools.dynamia.cms.site.payment

import org.springframework.context.annotation.Scope
import tools.dynamia.cms.site.payment.domain.ManualPayment
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
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
    private PaymentTransaction currentPaymentTransaction
    private ManualPayment currentManualPayment

    PaymentForm getCurrentPaymentForm() {
		return currentPaymentForm
    }

    void setCurrentPaymentForm(PaymentForm currentPaymentForm) {
		this.currentPaymentForm = currentPaymentForm
    }

    PaymentTransaction getCurrentPaymentTransaction() {
		return currentPaymentTransaction
    }

    void setCurrentPaymentTransaction(PaymentTransaction currentPaymentTransaction) {
		this.currentPaymentTransaction = currentPaymentTransaction
    }

    ManualPayment getCurrentManualPayment() {
		return currentManualPayment
    }

    void setCurrentManualPayment(ManualPayment currentManualPayment) {
		this.currentManualPayment = currentManualPayment
    }

}
