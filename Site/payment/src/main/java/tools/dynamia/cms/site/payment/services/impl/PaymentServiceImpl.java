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
package tools.dynamia.cms.site.payment.services.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.payment.PaymentException;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.PaymentUtils;
import tools.dynamia.cms.site.payment.api.PaymentSender;
import tools.dynamia.cms.site.payment.api.PaymentSenderException;
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus;
import tools.dynamia.cms.site.payment.api.Response;
import tools.dynamia.cms.site.payment.api.dto.ManualPaymentDTO;
import tools.dynamia.cms.site.payment.api.dto.PaymentDTO;
import tools.dynamia.cms.site.payment.domain.ManualPayment;
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.commons.BeanUtils;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.ApplicationParameters;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.DomainUtils;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Service;
import tools.dynamia.web.util.HttpRemotingServiceClient;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final String LAST_MANUAL_PAYMENT_NUMBER = "lastManualPaymentNumber-";

	@Autowired
	private CrudService crudService;

	@PersistenceContext
	private EntityManager em;

	private LoggingService logger = new SLF4JLoggingService(PaymentService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamia.cms.site.payment.services.impl.PaymentGatewayService#
	 * getGatewayConfigMap(com.dynamia.cms.site.payment.PaymentGateway,
	 * java.lang.String)
	 */
	@Override
	public Map<String, String> getGatewayConfigMap(PaymentGateway gateway, String source) {
		List<PaymentGatewayConfig> configs = crudService.find(PaymentGatewayConfig.class,
				QueryParameters.with("gatewayId", gateway.getId()).add("source", source));

		Map<String, String> map = new HashMap<String, String>();

		for (PaymentGatewayConfig pgc : configs) {
			map.put(pgc.getName(), pgc.getValue());
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamia.cms.site.payment.services.impl.PaymentGatewayService#
	 * getConfig (com.dynamia.cms.site.payment.PaymentGateway, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public PaymentGatewayConfig getConfig(PaymentGateway gateway, String name, String source) {
		return crudService.findSingle(PaymentGatewayConfig.class,
				QueryParameters.with("gatewayId", gateway.getId()).add("name", name).add("source", source));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dynamia.cms.site.payment.services.impl.PaymentGatewayService#
	 * addGatewayConfig(com.dynamia.cms.site.payment.PaymentGateway,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void addGatewayConfig(PaymentGatewayConfig cfg) {
		PaymentGateway gateway = findGateway(cfg.getGatewayId());
		PaymentGatewayConfig config = getConfig(gateway, cfg.getName(), cfg.getSource());
		if (config == null) {
			config = cfg;
		} else {
			config.setValue(cfg.getValue());
		}
		crudService.save(config);
	}

	@Override
	public PaymentGateway findGateway(String gatewayId) {
		for (PaymentGateway gateway : Containers.get().findObjects(PaymentGateway.class)) {
			if (gateway.getId().equals(gatewayId)) {
				return gateway;
			}
		}
		throw new PaymentException("PaymentGateway with id [" + gatewayId + "] not found");
	}

	@Override
	public PaymentTransaction findTransaction(PaymentGateway gateway, Map<String, String> response) {
		String uuid = gateway.locateTransactionId(response);
		if (uuid == null) {
			throw new PaymentException("No UUID for transaction found " + gateway.getId());
		}

		PaymentTransaction tx = crudService.findSingle(PaymentTransaction.class, QueryParameters
				.with("uuid", QueryConditions.eq(uuid)).add("gatewayId", QueryConditions.eq(gateway.getId())));

		if (tx == null) {
			throw new PaymentException("No transaction found for gateway " + gateway.getId() + " uuid: " + uuid);
		}

		return tx;

	}

	@Override
	public PaymentGateway getDefaultGateway() {
		Collection<PaymentGateway> gateways = Containers.get().findObjects(PaymentGateway.class);
		for (PaymentGateway paymentGateway : gateways) {
			return paymentGateway;
		}

		return null;
	}

	@Override
	public void saveTransaction(PaymentTransaction tx) {
		crudService.save(tx);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void register(ManualPayment payment) {
		if (payment.getNumber() == null && payment.getSource() != null) {
			String paramName = LAST_MANUAL_PAYMENT_NUMBER + payment.getSource();
			long lastNumber = Long.parseLong(ApplicationParameters.get().getValue(paramName, "0"));
			lastNumber++;

			ApplicationParameters.get().setParameter(paramName, lastNumber);

			String number = DomainUtils.formatNumberWithZeroes(lastNumber, 10000);
			payment.setNumber(number);
		}
		crudService.save(payment);
	}

	/**
	 * 
	 * @param source
	 * @param registratorCode
	 * @param payerCode
	 * @return
	 */
	@Override
	public List<ManualPayment> findManualPayments(String source, String registratorCode, String payerCode) {
		return crudService.find(ManualPayment.class,
				QueryParameters.with("source", source).add("registratorCode", registratorCode)
						.add("payerCode", payerCode).setAutocreateSearcheableStrings(false));

	}

	@Override
	public List<ManualPayment> findManualPaymentsByPayerId(String source, String payerId) {
		return crudService.find(ManualPayment.class,
				QueryParameters.with("source", source).add("payerId", payerId).setAutocreateSearcheableStrings(false));

	}

	/**
	 * 
	 * @param source
	 * @param payerCode
	 * @return
	 */
	@Override
	public List<ManualPayment> findManualPaymentsByPayerCode(String source, String payerCode) {
		return crudService.find(ManualPayment.class, QueryParameters.with("source", source).add("payerCode", payerCode)
				.setAutocreateSearcheableStrings(false));

	}

	@Override
	public void sendManualPayments(String source, String serviceUrl, Map<String, String> params) {
		try {
			logger.info("Sending all manual Payments");

			if (serviceUrl != null && !serviceUrl.isEmpty()) {
				PaymentSender sender = HttpRemotingServiceClient.build(PaymentSender.class).setServiceURL(serviceUrl)
						.getProxy();

				crudService.executeWithinTransaction(() -> {

					List<ManualPayment> payments = crudService.find(ManualPayment.class, QueryParameters
							.with("sended", false).add("source", source).setAutocreateSearcheableStrings(false));
					logger.info("Sending " + payments.size() + " manual payments for source " + source);

					sendPayments(sender, payments, params);
				});

			}
			logger.info("Manual payments Sended");
		} catch (Exception e) {
			logger.error("Error sending manual payments. Source: " + source + ", Service URL: " + serviceUrl, e);
		}
	}

	@Override
	public void sendPayments(String source, String serviceUrl, Map<String, String> params) {
		try {
			logger.info("Sending all auto Payments");

			if (serviceUrl != null && !serviceUrl.isEmpty()) {
				PaymentSender sender = HttpRemotingServiceClient.build(PaymentSender.class).setServiceURL(serviceUrl)
						.getProxy();

				crudService.executeWithinTransaction(() -> {

					List<PaymentTransaction> payments = crudService.find(PaymentTransaction.class, QueryParameters
							.with("sended", false).add("source", source)
							.add("status", PaymentTransactionStatus.COMPLETED)
							.add("confirmed",true)
							.add("test", false)
							.setAutocreateSearcheableStrings(false));
					logger.info("Sending " + payments.size() + " auto payments for source " + source);

					sendPaymentsTransaction(sender, payments, params);
				});

			}
			logger.info("Automatic payments Sended");
		} catch (Exception e) {
			logger.error("Error sending auto payments. Source: " + source + ", Service URL: " + serviceUrl, e);
		}

	}

	private void sendPayments(PaymentSender sender, List<ManualPayment> payments, Map<String, String> params) {
		List<ManualPaymentDTO> dtos = payments.stream().map(ord -> createDTO(ord)).collect(Collectors.toList());

		if (!dtos.isEmpty()) {
			logger.info("Calling Payment Sender " + sender);
			try {
				List<Response> response = sender.sendManualPayments(dtos, params);

				if (response != null) {
					logger.info("Sending response recieved. " + response.size());
					for (ManualPayment payment : payments) {
						Response resp = Response.find(response, payment.getNumber());
						if (resp != null) {
							if (resp.isError()) {
								payment.setErrorCode(resp.getErrorCode());
								payment.setErrorMessage(resp.getErrorMessage());
							} else {
								payment.setErrorCode(null);
								payment.setErrorMessage(null);
								payment.setExternalRef(resp.getContent());
								payment.setSended(true);
							}
						}
						crudService.update(payment);
						logger.info("==> Updating payment " + payment.getNumber() + " ==> " + resp);
					}
				}
			} catch (PaymentSenderException e) {
				logger.error("Error", e);
			}
		}
	}

	private void sendPaymentsTransaction(PaymentSender sender, List<PaymentTransaction> payments, Map<String, String> params) {
		List<PaymentDTO> dtos = payments.stream().map(ord -> createDTO(ord)).collect(Collectors.toList());

		if (!dtos.isEmpty()) {
			logger.info("Calling Payment Sender " + sender);
			try {
				List<Response> response = sender.sendPayments(dtos, params);

				if (response != null) {
					logger.info("Sending response recieved. " + response.size());
					for (PaymentTransaction payment : payments) {
						Response resp = Response.find(response, payment.getUuid());
						if (resp != null) {
							if (resp.isError()) {
								payment.setErrorCode(resp.getErrorCode());
								payment.setErrorMessage(resp.getErrorMessage());
							} else {
								payment.setErrorCode(null);
								payment.setErrorMessage(null);
								payment.setExternalRef(resp.getContent());
								payment.setSended(true);
							}
						}
						crudService.update(payment);
						logger.info("==> Updating payment " + payment.getUuid() + " ==> " + resp);
					}
				}
			} catch (PaymentSenderException e) {
				logger.error("Error", e);
			}
		}
	}

	private ManualPaymentDTO createDTO(ManualPayment ord) {
		ManualPaymentDTO dto = new ManualPaymentDTO();
		BeanUtils.setupBean(dto, ord);
		return dto;
	}

	private PaymentDTO createDTO(PaymentTransaction ord) {
		PaymentDTO dto = new PaymentDTO();
		BeanUtils.setupBean(dto, ord);
		return dto;
	}
}
