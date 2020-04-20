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
