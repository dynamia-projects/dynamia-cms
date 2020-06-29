/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
                      @RequestParam(value = "i", required = false) String identification,
                      @RequestParam(value = "n", required = false) String name,
                      @RequestParam(value = "d") String description,
                      @RequestParam(value = "e", required = false) String email,
                      @RequestParam(value = "ph", required = false) String phone,
                      @RequestParam(value = "mb", required = false) String mobile,
                      @RequestParam(value = "cr", required = false) String currency,
                      @RequestParam(value = "aid", required = false) String accountId,
                      @RequestParam(value = "from", required = false) String from,
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
            tx.from = from
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

            if (tx.payerDocument == null || tx.payerFullname == null || tx.email == null) {
                mv.setViewName("payment/linkForm")
            }
        } else {
            throw new PaymentException("No payment gateway found for source $source ")
        }

        return mv
    }

}
