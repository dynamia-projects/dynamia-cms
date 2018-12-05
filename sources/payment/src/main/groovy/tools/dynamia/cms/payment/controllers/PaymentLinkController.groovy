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
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/payment")
class PaymentLinkController {

    @Autowired
    private PaymentService paymentService

    @GetMapping(value = "/link")
    ModelAndView link(@RequestParam(value = "doc", required = false) String document,
                      @RequestParam("v") BigDecimal value,
                      @RequestParam("i") String identification,
                      @RequestParam("n") String name,
                      @RequestParam(value = "d", required = false) String description,
                      @RequestParam("e") String email,
                      @RequestParam(value = "ph", required = false) String phone,
                      @RequestParam(value = "mb", required = false) String mobile,
                      @RequestParam(value = "cr", required = false) String currency,
                      @RequestParam(value = "aid", required = false) String accountId,
                      HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("payment/link")
        PaymentSource source = paymentService.findPaymentSource(request)

        if (source != null) {
            PaymentGatewayAccount account = accountId == null ? paymentService.getDefaultAccount(source.name) : paymentService.findAccount(accountId)

            if (account == null) {
                throw new PaymentException("No account found for payment link")
            }

            PaymentGateway gateway = paymentService.findGateway(account.gatewayId)
            PaymentTransaction tx = gateway.newTransaction(account, source.baseURL)
            tx.status = PaymentTransactionStatus.NEW
            tx.gatewayId = account.gatewayId
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
            throw new PaymentException("No payment gateway found for source $source ")
        }

        return mv
    }

}
