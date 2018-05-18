package tools.dynamia.cms.payment.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.payment.PaymentException
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.payment.api.PaymentSource
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentTransaction

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/payment")
class PaymentLinkController {

    @Autowired
    private tools.dynamia.cms.payment.services.PaymentService paymentService

    @GetMapping(value = "/link/{gatewayId}")
    ModelAndView link(@PathVariable String gatewayId,
                      @RequestParam(value = "doc", required = false) String document,
                      @RequestParam("v") BigDecimal value,
                      @RequestParam("i") String identification,
                      @RequestParam("n") String name,
                      @RequestParam(value = "d", required = false) String description,
                      @RequestParam("e") String email,
                      @RequestParam(value = "ph", required = false) String phone,
                      @RequestParam(value = "mb", required = false) String mobile,
                      @RequestParam(value = "cr", required = false) String currency,
                      HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("payment/link")

        PaymentSource source = paymentService.findPaymentSource(request)
        if (source != null) {
            PaymentGateway gateway = paymentService.findGateway(gatewayId)
            PaymentTransaction tx = gateway.newTransaction(source.name, source.baseURL)
            tx.status = PaymentTransactionStatus.NEW
            tx.gatewayId = gatewayId
            tx.amount = value
            tx.description = document + " - " + description
            tx.email = email
            tx.payerDocument = identification
            tx.payerFullname = name
            tx.document = document
            tx.payerMobileNumber = mobile
            tx.payerPhoneNumber = phone
            if (currency == null || currency.empty) {
                currency = source.currency
            }

            if (currency == null || currency.empty) {
                throw new PaymentException("No currency for payment link provided")
            }

            tx.currency = currency
            mv.addObject("transaction", tx)
            mv.addObject("title", "Payment")


            PaymentForm form = gateway.createForm(tx)
            mv.addObject("paymentForm", form)

            PaymentHolder.get().currentPaymentForm = form
            PaymentHolder.get().currentPaymentTransaction = tx
        } else {
            throw new PaymentException("No payment gateway found: " + gatewayId)
        }

        return mv
    }

}
