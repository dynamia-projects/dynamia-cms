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
package tools.dynamia.cms.site.payment.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.PaymentTransactionEvent;
import tools.dynamia.cms.site.payment.PaymentTransactionListener;
import tools.dynamia.cms.site.payment.ResponseType;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import tools.dynamia.cms.site.payment.services.PaymentService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService service;

	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(PaymentController.class, "[==PAYMENT==] ");

	@RequestMapping(value = "/{gatewayId}/response", method = RequestMethod.GET)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ModelAndView gatewayResponse(@PathVariable String gatewayId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("payment/response");

		try {
			PaymentTransaction tx = commitTransaction(gatewayId, request, ResponseType.RESPONSE);
			crudService.save(tx);
			mv.addObject("title", tx.getStatusText());
			mv.addObject("subtitle", tx.getReference());
			mv.addObject("transaction", tx);
			mv.addObject("gateway", service.findGateway(gatewayId));
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("redirect:/");
		}
		return mv;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{gatewayId}/confirmation", method = { RequestMethod.POST, RequestMethod.GET })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void gatewayConfirmation(@PathVariable String gatewayId, HttpServletRequest request) {
		logger.info("Confirmation for gateway " + gatewayId + "  REQUEST: " + request.getParameterMap());
		PaymentTransaction tx = commitTransaction(gatewayId, request, ResponseType.CONFIRMATION);
		if (!tx.isConfirmed()) {
			tx.setConfirmed(true);
			logger.info("Payment Transaction Confirmation " + tx.getUuid() + " - " + tx.getStatusText());
			crudService.save(tx);
			logger.info("Payment Transaaction confirmed - OK");
		}

	}

	private PaymentTransaction commitTransaction(String gatewayId, HttpServletRequest request, ResponseType type) {

		PaymentGateway gateway = service.findGateway(gatewayId);
		Map<String, String> response = parseRequest(gateway.getResponseParams(), request);

		PaymentTransaction tx = service.findTransaction(gateway, response);
		logger.info("Commiting payment Transaction " + tx.getUuid());
		if (!tx.isConfirmed()) {
			logger.info("Transaction " + tx + " is not confirmed, processing response..");
			PaymentTransactionStatus oldStatus = tx.getStatus();

			gateway.processResponse(tx, response, type);

			if (oldStatus != tx.getStatus()) {
				logger.info("Firing status listener for transaction " + tx + "  new status: " + tx.getStatus() + " - "
						+ tx.getStatusText());
				fireNewStatusListeners(tx, oldStatus);
			}
		}

		return tx;
	}

	private void fireNewStatusListeners(PaymentTransaction tx, PaymentTransactionStatus oldStatus) {
		logger.info("Firing payment transactions listeners for " + tx.getUuid());
		for (PaymentTransactionListener listener : Containers.get().findObjects(PaymentTransactionListener.class)) {
			try {
				listener.onStatusChanged(new PaymentTransactionEvent(tx, oldStatus));
			} catch (Exception e) {
				logger.error("Error firing " + PaymentTransactionListener.class.getSimpleName() + " " + listener, e);
			}
		}
		logger.info("==>Listeners Completed.");

	}

	private Map<String, String> parseRequest(String[] params, HttpServletRequest request) {
		Map<String, String> response = new HashMap<String, String>();
		for (String name : params) {
			String value = request.getParameter(name);
			response.put(name, value);
		}
		return response;
	}

}
