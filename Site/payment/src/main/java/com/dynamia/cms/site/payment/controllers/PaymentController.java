package com.dynamia.cms.site.payment.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.PaymentTransactionEvent;
import com.dynamia.cms.site.payment.PaymentTransactionListener;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Controller;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService service;

	private LoggingService logger = new SLF4JLoggingService(PaymentController.class);

	@RequestMapping(value = "/{gatewayId}/response", method = RequestMethod.GET)
	public ModelAndView gatewayResponse(@PathVariable String gatewayId, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("paymentResponse");

		PaymentTransaction tx = commitTransaction(gatewayId, request);

		mv.addObject("transaction", tx);
		mv.addObject("gateway", service.findGateway(gatewayId));
		return mv;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{gatewayId}/response", method = RequestMethod.POST)
	public void gatewayConfirmation(@PathVariable String gatewayId, HttpServletRequest request) {
		commitTransaction(gatewayId, request);
	}

	private PaymentTransaction commitTransaction(String gatewayId, HttpServletRequest request) {
		Map<String, String> response = parseRequest(request);

		PaymentGateway gateway = service.findGateway(gatewayId);
		PaymentTransaction tx = service.findTransaction(gateway, response);
		PaymentTransactionStatus oldStatus = tx.getStatus();

		gateway.commit(tx, response);

		if (oldStatus != tx.getStatus()) {
			fireNewStatusListeners(tx, oldStatus);
		}

		return tx;
	}

	private void fireNewStatusListeners(PaymentTransaction tx, PaymentTransactionStatus oldStatus) {
		for (PaymentTransactionListener listener : Containers.get().findObjects(PaymentTransactionListener.class)) {
			try {
				listener.onStatusChanged(new PaymentTransactionEvent(tx, oldStatus));
			} catch (Exception e) {
				logger.error("Error firing " + PaymentTransactionListener.class.getSimpleName() + " " + listener, e);
			}
		}

	}

	private Map<String, String> parseRequest(HttpServletRequest request) {
		Map<String, String> response = new HashMap<String, String>();
		while (request.getParameterNames().hasMoreElements()) {
			String name = request.getParameterNames().nextElement();
			String value = request.getParameter(name);
			response.put(name, value);
		}
		return response;
	}

}
