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
