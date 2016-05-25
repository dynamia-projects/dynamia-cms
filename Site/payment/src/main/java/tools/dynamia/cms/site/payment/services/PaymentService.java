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
package tools.dynamia.cms.site.payment.services;

import java.util.Map;

import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;

public interface PaymentService {

	public abstract Map<String, String> getGatewayConfigMap(PaymentGateway gateway, String source);

	public abstract PaymentGatewayConfig getConfig(PaymentGateway gateway, String name, String source);

	public abstract void addGatewayConfig(PaymentGatewayConfig config);

	public abstract PaymentGateway findGateway(String gatewayId);

	public abstract PaymentTransaction findTransaction(PaymentGateway gateway, Map<String, String> response);

	public abstract PaymentGateway getDefaultGateway();

}