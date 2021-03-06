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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.payment.PaymentTransactionEvent
import tools.dynamia.cms.payment.PaymentTransactionListener
import tools.dynamia.cms.payment.ResponseType
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/payment")
class PaymentController {

    @Autowired
    private PaymentService service

    @Autowired
    private CrudService crudService

    private LoggingService logger = new SLF4JLoggingService(PaymentController.class)

    @RequestMapping(value = "/{gatewayId}/response", method = [RequestMethod.GET, RequestMethod.POST])
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    ModelAndView gatewayResponse(@PathVariable String gatewayId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("payment/response")

        try {
            PaymentTransaction tx = commitTransaction(gatewayId, request, ResponseType.RESPONSE)
            mv.addObject("title", tx.statusText)
            mv.addObject("subtitle", tx.reference)
            mv.addObject("transaction", tx)
            mv.addObject("status",tx.status)
            mv.addObject("gateway", service.findGateway(gatewayId))
        } catch (Exception e) {
            e.printStackTrace()
            mv.view = new RedirectView("/", true, true, false)

        }
        return mv
    }

    @RequestMapping(value = "/{gatewayId}/confirmation", method = [RequestMethod.POST, RequestMethod.GET])
    @Transactional
    ResponseEntity<String> gatewayConfirmation(@PathVariable String gatewayId, HttpServletRequest request) {
        try {
            logger.info("Confirmation for gateway " + gatewayId + "  REQUEST: " + request.parameterMap)
            PaymentTransaction tx = commitTransaction(gatewayId, request, ResponseType.CONFIRMATION)
            logger.info("Payment Transaction Confirmation Status " + tx)
            return ResponseEntity.ok("TX " + tx.uuid + " STATUS: " + tx.status.toString())
        } catch (Exception e) {
            logger.error("Error processing tx confirmation. GatewayID: " + gatewayId + ". Error: " + e.message, e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gateway " + gatewayId + " -->" + e.message)
        }

    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    ModelAndView startTransaction() {
        logger.info("Starting payment transaction..")
        PaymentTransaction tx = PaymentHolder.get().currentPaymentTransaction
        PaymentForm form = PaymentHolder.get().currentPaymentForm
        ModelAndView mv = new ModelAndView("payment/start")
        mv.addObject("title", "Starting Payment")
        if (tx != null && tx.status == PaymentTransactionStatus.NEW && form != null) {
            logger.info("Processing transaction " + tx.uuid + " from " + tx.source)
            tx.status = PaymentTransactionStatus.PROCESSING
            crudService.save(tx)
            mv.addObject("paymentForm", form)
            mv.addObject("paymentTransaction", tx)
            logger.info("Redirecting to payment gateway site. TX: " + tx)
        } else {
            logger.warn("Payment Transaction Not Valid")
            mv.view = new RedirectView("/", true, true, false)
        }
        return mv
    }

    private PaymentTransaction commitTransaction(String gatewayId, HttpServletRequest request, ResponseType type) {

        PaymentGateway gateway = service.findGateway(gatewayId)
        Map<String, String> response = parseRequest(gateway.responseParams, request)
        System.out.println(request.parameterMap)
        PaymentTransaction tx = service.findTransaction(gateway, response)

        System.out.println(request.parameterNames)

        logger.info("========================================================")
        logger.info("Commiting payment Transaction " + tx.uuid + " from " + tx.source)
        if (!tx.confirmed) {
            tx.responseTries = tx.responseTries + 1

            logger.info("Transaction " + tx + " is not confirmed, processing response..")
            PaymentTransactionStatus oldStatus = tx.status
            gateway.processResponse(tx, response, type)

            if (oldStatus != tx.status) {
                logger.info("Firing status listener for transaction " + tx + "  new status: " + tx.status + " - "
                        + tx.statusText)
                fireNewStatusListeners(tx, oldStatus)
            }
        }

        logger.info("TX SOURCE: " + tx.source)
        logger.info("========================================================")
        return tx
    }

    private void fireNewStatusListeners(PaymentTransaction tx, PaymentTransactionStatus oldStatus) {
        logger.info("Firing payment transactions listeners for " + tx.uuid)
        for (PaymentTransactionListener listener : Containers.get().findObjects(PaymentTransactionListener.class)) {
            try {
                listener.onStatusChanged(new PaymentTransactionEvent(tx, oldStatus))
            } catch (Exception e) {
                logger.error("Error firing " + PaymentTransactionListener.class.simpleName + " " + listener, e)
            }
        }
        logger.info("==>Listeners Completed.")

    }

    private Map<String, String> parseRequest(String[] params, HttpServletRequest request) {
        Map<String, String> response = new HashMap<String, String>()
        for (String name : params) {
            String value = request.getParameter(name)
            response.put(name, value)
        }
        return response
    }

}
