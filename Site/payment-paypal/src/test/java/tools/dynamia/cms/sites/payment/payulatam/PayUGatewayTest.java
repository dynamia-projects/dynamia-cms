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
package tools.dynamia.cms.sites.payment.payulatam;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import tools.dynamia.cms.site.payment.ResponseType;
import tools.dynamia.cms.site.payment.paypal.PaypalGateway;
import tools.dynamia.cms.site.payment.services.impl.PaymentServiceImpl;

public class PayUGatewayTest {

	@Test
	public void testResponseSignature() {
		String apiKey = "6u39nqhq8ftd0hlvnjfs66eh8c";
		Map<String, String> response = new HashMap<String, String>();
		response.put("merchantId", "500238");
		response.put("referenceCode", "580C965A81474DAE99DB4ED043FDDF9D");
		response.put("TX_VALUE", "1990000.00");
		response.put("currency", "COP");
		response.put("state_pol", "6");
		response.put("signature", "15b4a4be299872d8cff29707ace8f7df");

		PaypalGateway gateway = new PaypalGateway(new PaymentServiceImpl());

		Assert.assertTrue(gateway.isValidSignature(apiKey, response,ResponseType.CONFIRMATION));
	}

}
