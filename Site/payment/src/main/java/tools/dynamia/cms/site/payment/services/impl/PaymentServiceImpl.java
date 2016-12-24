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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.payment.PaymentException;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private CrudService crudService;

	@PersistenceContext
	private EntityManager em;

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
}
