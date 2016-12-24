package tools.dynamia.cms.site.payment;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;

import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Component;

@Component
@Scope("session")
public class PaymentHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6257694168919195864L;

	public static PaymentHolder get() {
		return Containers.get().findObject(PaymentHolder.class);
	}

	private PaymentForm currentPaymentForm;
	private PaymentTransaction currentPaymentTransaction;

	public PaymentForm getCurrentPaymentForm() {
		return currentPaymentForm;
	}

	public void setCurrentPaymentForm(PaymentForm currentPaymentForm) {
		this.currentPaymentForm = currentPaymentForm;
	}

	public PaymentTransaction getCurrentPaymentTransaction() {
		return currentPaymentTransaction;
	}

	public void setCurrentPaymentTransaction(PaymentTransaction currentPaymentTransaction) {
		this.currentPaymentTransaction = currentPaymentTransaction;
	}

}