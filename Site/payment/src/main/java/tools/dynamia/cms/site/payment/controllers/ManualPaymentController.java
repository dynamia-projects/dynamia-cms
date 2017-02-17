package tools.dynamia.cms.site.payment.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.payment.PaymentHolder;
import tools.dynamia.cms.site.payment.domain.ManualPayment;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.domain.ValidationError;

@Controller
@RequestMapping("/payment")
public class ManualPaymentController {

	@Autowired
	private PaymentService service;

	@RequestMapping(value = "/manual/register", method = RequestMethod.POST)
	public ModelAndView register(ManualPayment payment, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payment/manual");

		try {
			ManualPayment currentPay = PaymentHolder.get().getCurrentManualPayment();
			if (currentPay != null) {
				currentPay.setReference(payment.getReference());
				currentPay.setReference2(payment.getReference2());
				currentPay.setReference3(payment.getReference3());
				currentPay.setType(payment.getType());
				currentPay.setAmount(payment.getAmount());
				currentPay.setDescription(payment.getDescription());
				service.register(currentPay);
				mv.addObject("payment", currentPay);
				mv.addObject("title","Estado de Pago");
				PaymentHolder.get().setCurrentManualPayment(null);
			}

		} catch (ValidationError e) {
			mv.addObject("errormessage", e.getMessage());
		}

		return mv;
	}
}
