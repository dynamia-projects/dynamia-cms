package tools.dynamia.cms.site.payment.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.payment.PaymentException
import tools.dynamia.cms.site.payment.PaymentForm
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.PaymentHolder
import tools.dynamia.cms.site.payment.api.PaymentSource
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
import tools.dynamia.cms.site.payment.services.PaymentService

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/payment")
public class PaymentLinkController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/link/{gatewayId}")
    public ModelAndView link(@PathVariable String gatewayId,
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
        ModelAndView mv = new ModelAndView("payment/link");

        PaymentSource source = paymentService.findPaymentSource(request);
        if (source != null) {
            PaymentGateway gateway = paymentService.findGateway(gatewayId);
            PaymentTransaction tx = gateway.newTransaction(source.getName(), source.getBaseURL());
            tx.setStatus(PaymentTransactionStatus.NEW);
            tx.setGatewayId(gatewayId);
            tx.setAmount(value);
            tx.setDescription(document + " - " + description);
            tx.setEmail(email);
            tx.setPayerDocument(identification);
            tx.setPayerFullname(name);
            tx.setDocument(document);
            tx.setPayerMobileNumber(mobile);
            tx.setPayerPhoneNumber(phone);
            if (currency == null || currency.isEmpty()) {
                currency = source.getCurrency();
            }

            if (currency == null || currency.isEmpty()) {
                throw new PaymentException("No currency for payment link provided");
            }

            tx.setCurrency(currency);
            mv.addObject("transaction", tx);
            mv.addObject("title", "Payment");


            PaymentForm form = gateway.createForm(tx);
            mv.addObject("paymentForm", form);

            PaymentHolder.get().setCurrentPaymentForm(form);
            PaymentHolder.get().setCurrentPaymentTransaction(tx);
        } else {
            throw new PaymentException("No payment gateway found: " + gatewayId);
        }

        return mv;
    }

}
