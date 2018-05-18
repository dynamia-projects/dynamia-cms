package tools.dynamia.cms.payment.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.domain.ValidationError

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/payment")
class ManualPaymentController {

    @Autowired
    private tools.dynamia.cms.payment.services.PaymentService service

    @RequestMapping(value = "/manual/register", method = RequestMethod.POST)
    ModelAndView register(ManualPayment payment, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("payment/manual")

        try {
            ManualPayment currentPay = PaymentHolder.get().currentManualPayment
            if (currentPay != null) {
                currentPay.reference = payment.reference
                currentPay.reference2 = payment.reference2
                currentPay.reference3 = payment.reference3
                currentPay.type = payment.type
                currentPay.amount = payment.amount
                currentPay.description = payment.description
                service.register(currentPay)
                mv.addObject("payment", currentPay)
                mv.addObject("title", "Estado de Pago")
                PaymentHolder.get().currentManualPayment = null
            }

        } catch (ValidationError e) {
            mv.addObject("errormessage", e.message)
        }

        return mv
    }
}
