package com.dynamia.cms.site.payment.services.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.cms.site.payment.PaymentException;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Service;

@Service
class PaymentServiceImpl implements PaymentService {

	@Autowired
	private CrudService crudService;

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
	 * @see
	 * com.dynamia.cms.site.payment.services.impl.PaymentGatewayService#getConfig
	 * (com.dynamia.cms.site.payment.PaymentGateway, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public PaymentGatewayConfig getConfig(PaymentGateway gateway, String name, String source) {
		return crudService.findSingle(PaymentGatewayConfig.class,
				QueryParameters.with("gatewayId", gateway.getId())
						.add("name", name)
						.add("source", source));
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
		String uuid = response.get(gateway.getTransactionLocator());
		PaymentTransaction tx = crudService.findSingle(PaymentTransaction.class,
				QueryParameters.with("uuid", uuid).add("gatewayId", gateway.getId()));

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
}
