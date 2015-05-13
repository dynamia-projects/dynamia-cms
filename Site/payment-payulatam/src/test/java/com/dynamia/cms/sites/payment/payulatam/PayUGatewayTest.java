package com.dynamia.cms.sites.payment.payulatam;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.dynamia.cms.site.payment.payulatam.PayULatamGateway;

public class PayUGatewayTest {

	@Test
	public void testResponseSignature() {
		String apiKey = "6u39nqhq8ftd0hlvnjfs66eh8c";
		Map<String, String> response = new HashMap<String, String>();
		response.put("merchantId", "500238");
		response.put("referenceCode", "580C965A81474DAE99DB4ED043FDDF9D");
		response.put("TX_VALUE", "1990000.00");
		response.put("currency", "COP");
		response.put("transactionState", "6");
		response.put("signature", "15b4a4be299872d8cff29707ace8f7df");

		PayULatamGateway gateway = new PayULatamGateway();

		Assert.assertTrue(gateway.isValidSignature(apiKey, response));
	}

}
