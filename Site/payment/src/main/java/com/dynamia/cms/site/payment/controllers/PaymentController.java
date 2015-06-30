package com.dynamia.cms.site.payment.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.PaymentTransactionEvent;
import com.dynamia.cms.site.payment.PaymentTransactionListener;
import com.dynamia.cms.site.payment.ResponseType;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Controller;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService service;

	@Autowired
	private CrudService crudService;

	private LoggingService logger = new SLF4JLoggingService(PaymentController.class);

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
			mv.setViewName("redirect:/");
		}
		return mv;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{gatewayId}/confirmation", method = { RequestMethod.POST, RequestMethod.GET })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void gatewayConfirmation(@PathVariable String gatewayId, HttpServletRequest request) {

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
			PaymentTransactionStatus oldStatus = tx.getStatus();

			gateway.processResponse(tx, response, type);

			if (oldStatus != tx.getStatus()) {
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
