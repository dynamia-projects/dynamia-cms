/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.payment.controllers

import java.util.HashMap
import java.util.Map

import javax.servlet.http.HttpServletRequest

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

import tools.dynamia.cms.site.payment.PaymentForm
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.PaymentHolder
import tools.dynamia.cms.site.payment.PaymentTransactionEvent
import tools.dynamia.cms.site.payment.PaymentTransactionListener
import tools.dynamia.cms.site.payment.ResponseType
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

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
            mv.addObject("title", tx.getStatusText())
            mv.addObject("subtitle", tx.getReference())
            mv.addObject("transaction", tx)
            mv.addObject("gateway", service.findGateway(gatewayId))
        } catch (Exception e) {
            e.printStackTrace()
            mv.setView(new RedirectView("/", true, true, false))

        }
        return mv
    }

    @RequestMapping(value = "/{gatewayId}/confirmation", method = [RequestMethod.POST, RequestMethod.GET])
    @Transactional
    ResponseEntity<String> gatewayConfirmation(@PathVariable String gatewayId, HttpServletRequest request) {
        try {
            logger.info("Confirmation for gateway " + gatewayId + "  REQUEST: " + request.getParameterMap())
            PaymentTransaction tx = commitTransaction(gatewayId, request, ResponseType.CONFIRMATION)
            logger.info("Payment Transaction Confirmation Status " + tx)
            return ResponseEntity.ok("TX " + tx.getUuid() + " STATUS: " + tx.getStatus().toString())
        } catch (Exception e) {
            logger.error("Error processing tx confirmation. GatewayID: " + gatewayId + ". Error: " + e.getMessage(), e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gateway " + gatewayId + " -->" + e.getMessage())
        }

    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    ModelAndView startTransaction() {
        logger.info("Starting payment transaction..")
        PaymentTransaction tx = PaymentHolder.get().getCurrentPaymentTransaction()
        PaymentForm form = PaymentHolder.get().getCurrentPaymentForm()
        ModelAndView mv = new ModelAndView("payment/start")
        if (tx != null && tx.getStatus() == PaymentTransactionStatus.NEW && form != null) {
            logger.info("Processing transaction " + tx.getUuid() + " from " + tx.getSource())
            tx.setStatus(PaymentTransactionStatus.PROCESSING)
            crudService.save(tx)
            mv.addObject("paymentForm", form)
            mv.addObject("paymentTransaction", tx)
            logger.info("Redirecting to payment gateway site. TX: " + tx)
        } else {
            logger.warn("Payment Transaction Not Valid")
            mv.setView(new RedirectView("/", true, true, false))
        }
        return mv
    }

    private PaymentTransaction commitTransaction(String gatewayId, HttpServletRequest request, ResponseType type) {

        PaymentGateway gateway = service.findGateway(gatewayId)
        Map<String, String> response = parseRequest(gateway.getResponseParams(), request)
        System.out.println(request.getParameterMap())
        PaymentTransaction tx = service.findTransaction(gateway, response)

        System.out.println(request.getParameterNames())

        logger.info("========================================================")
        logger.info("Commiting payment Transaction " + tx.getUuid() + " from " + tx.getSource())
        if (!tx.isConfirmed()) {
            tx.setResponseTries(tx.getResponseTries() + 1)

            logger.info("Transaction " + tx + " is not confirmed, processing response..")
            PaymentTransactionStatus oldStatus = tx.getStatus()
            gateway.processResponse(tx, response, type)

            if (oldStatus != tx.getStatus()) {
                logger.info("Firing status listener for transaction " + tx + "  new status: " + tx.getStatus() + " - "
                        + tx.getStatusText())
                fireNewStatusListeners(tx, oldStatus)
            }
        }

        logger.info("TX SOURCE: " + tx.getSource())
        logger.info("========================================================")
        return tx
    }

    private void fireNewStatusListeners(PaymentTransaction tx, PaymentTransactionStatus oldStatus) {
        logger.info("Firing payment transactions listeners for " + tx.getUuid())
        for (PaymentTransactionListener listener : Containers.get().findObjects(PaymentTransactionListener.class)) {
            try {
                listener.onStatusChanged(new PaymentTransactionEvent(tx, oldStatus))
            } catch (Exception e) {
                logger.error("Error firing " + PaymentTransactionListener.class.getSimpleName() + " " + listener, e)
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
