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
package tools.dynamia.cms.site.payment.paypal;

import static tools.dynamia.cms.site.payment.PaymentUtils.mapToString;
import static tools.dynamia.cms.site.payment.PaymentUtils.md5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.payment.PaymentException;
import tools.dynamia.cms.site.payment.PaymentForm;
import tools.dynamia.cms.site.payment.PaymentGateway;
import tools.dynamia.cms.site.payment.ResponseType;
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus;
import tools.dynamia.cms.site.payment.domain.PaymentTransaction;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

@Service
public class PaypalGateway implements PaymentGateway {

	private static final String PAYER_FULL_NAME = "payerFullName";
	private static final String PAYER_DOCUMENT = "payerDocument";
	private static final String PAYER_MOBILE_PHONE = "payerMobilePhone";
	private static final String PAYER_PHONE = "payerPhone";
	private static final String PAYER_EMAIL = "payerEmail";
	private static final String BUYER_FULL_NAME = "buyerFullName";
	private static final String TX_STATE = "transactionState";
	private static final String TX_VALUE = "TX_VALUE";
	private static final String RES_PSE_REFERENCE3 = "pseReference3";
	private static final String RES_PSE_REFERENCE2 = "pseReference2";
	private static final String RES_PSE_REFERENCE1 = "pseReference1";
	private static final String RES_CUS = "cus";
	private static final String RES_REFERENCE_POL = "reference_pol";
	private static final String RES_LAP_PAYMENT_METHOD = "lapPaymentMethod";
	private static final String RES_PSE_BANK = "pseBank";
	private static final String RES_POL_RESPONSE_CODE = "polResponseCode";
	private static final String RES_TRANSACTION_STATE = "transactionState";

	private static final String CONFIRMATION_URL = "confirmationUrl";
	private static final String RESPONSE_URL = "responseUrl";
	private static final String DESCRIPTION = "description";
	private static final String API_KEY = "apiKey";
	private static final String ACCOUNT_ID = "accountId";
	private static final String MERCHANT_ID = "merchantId";
	private static final String SIGNATURE = "signature";
	private static final String BUYER_EMAIL = "buyerEmail";
	private static final String TEST = "test";
	private static final String CURRENCY = "currency";
	private static final String TAX_RETURN_BASE = "taxReturnBase";
	private static final String TAX = "tax";
	private static final String AMOUNT = "amount";
	private static final String REFERENCE_CODE = "referenceCode";
	private static final String REFERENCE_SALE = "reference_sale";
	private static final String TEST_URL = "testUrl";
	private static final String PRODUCTION_URL = "productionUrl";
	private static final String RES_STATE_POL = "state_pol";

	private PaymentService service;

	private LoggingService logger = new SLF4JLoggingService(PaypalGateway.class, "[PAYULATAM] ");

	@Autowired
	public PaypalGateway(PaymentService service) {
		super();
		this.service = service;

	}

	@Override
	public String getName() {
		return "Paypal";
	}

	@Override
	public String getId() {
		return "paypal";
	}

	@Override
	public String locateTransactionId(Map<String, String> response) {
		String uuid = null;
		if (response != null) {
			uuid = response.get(REFERENCE_CODE);
			if (uuid == null) {
				uuid = response.get(REFERENCE_SALE);
			}

		}

		return uuid;
	}

	@Override
	public String[] getRequiredParams() {
		return new String[] { API_KEY, ACCOUNT_ID, MERCHANT_ID, TEST, TEST_URL, PRODUCTION_URL };
	}

	@Override
	public String[] getResponseParams() {
		return new String[] { RES_CUS, RES_LAP_PAYMENT_METHOD, RES_POL_RESPONSE_CODE, RES_TRANSACTION_STATE,
				RES_PSE_BANK, RES_PSE_REFERENCE1, RES_PSE_REFERENCE2, RES_PSE_REFERENCE3, RES_REFERENCE_POL, SIGNATURE,
				REFERENCE_CODE, MERCHANT_ID, ACCOUNT_ID, TAX_RETURN_BASE, TX_STATE, TX_VALUE, CURRENCY, RES_STATE_POL,
				REFERENCE_SALE };
	}

	@Override
	public PaymentTransaction newTransaction(String source, String baseURL) {
		PaymentTransaction tx = new PaymentTransaction(source);

		tx.setResponseURL(baseURL + "payment/" + getId() + "/response");
		tx.setConfirmationURL(baseURL + "payment/" + getId() + "/confirmation");
		return tx;
	}

	@Override
	public PaymentForm createForm(PaymentTransaction tx) {
		Map<String, String> params = service.getGatewayConfigMap(this, tx.getSource());
		PaymentForm form = new PaymentForm();
		form.setHttpMethod("post");
		
		boolean test = false;
		if (!"1".equals(params.get(TEST))) {
			test = tx.isTest();
		} else {
			test = true;
		}
		if (test) {
			form.setUrl(params.get(TEST_URL));
		} else {
			form.setUrl(params.get(PRODUCTION_URL));
		}

		DecimalFormat formatter = new DecimalFormat("######");

		if (!"1".equals(params.get(TEST))) {
			form.addParam(TEST, tx.isTest() ? "1" : "0");
		} else {
			form.addParam(TEST, "1");
		}

		form.addParam(MERCHANT_ID, params.get(MERCHANT_ID));
		form.addParam(ACCOUNT_ID, params.get(ACCOUNT_ID));
		form.addParam(REFERENCE_CODE, tx.getUuid());
		form.addParam(AMOUNT, formatter.format(tx.getAmount()));
		form.addParam(TAX, formatter.format(tx.getTaxes()));
		form.addParam(TAX_RETURN_BASE, formatter.format(tx.getTaxesBase()));

		if (tx.getCurrency() == null || tx.getCurrency().isEmpty()) {
			throw new PaymentException("No Currency supplied for PayU");
		}
		form.addParam(CURRENCY, tx.getCurrency());

		form.addParam(BUYER_EMAIL, tx.getEmail());
		form.addParam(BUYER_FULL_NAME, tx.getPayerFullname());
		form.addParam(PAYER_EMAIL, tx.getEmail());
		form.addParam(PAYER_PHONE, tx.getPayerPhoneNumber());
		form.addParam(PAYER_MOBILE_PHONE, tx.getPayerMobileNumber());
		form.addParam(PAYER_DOCUMENT, tx.getPayerDocument());
		form.addParam(PAYER_FULL_NAME, tx.getPayerFullname());

		form.addParam(RESPONSE_URL, tx.getResponseURL());
		form.addParam(CONFIRMATION_URL, tx.getConfirmationURL());
		form.addParam(DESCRIPTION, tx.getDescription());
		tx.setSignature(generateSignature(params.get(API_KEY), form.getParameters()));
		form.addParam(SIGNATURE, tx.getSignature());

		return form;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {
		Map<String, String> params = service.getGatewayConfigMap(this, tx.getSource());

		if (tx.getEndDate() == null) {
			tx.setEndDate(new Date());
		}

		if (!isValidSignature(params.get(API_KEY), response, type)) {
			tx.setStatus(PaymentTransactionStatus.ERROR);
			tx.setStatusText("Firma digital invalida");
			service.saveTransaction(tx);
			logger.error("Signature is invalid");
			return false;
		}

		String txState = getResponseState(response, type);

		if (txState == null || txState.isEmpty()) {
			logger.info("NO Transaction State found in response aborting TX: " + tx.getUuid());
			return false;
		}

		String statusText = "";
		PaymentTransactionStatus status = tx.getStatus();
		if (status == null || status != PaymentTransactionStatus.COMPLETED) {

			if (txState.equals("4")) {
				statusText = "Transaccion Aprobada";
				status = PaymentTransactionStatus.COMPLETED;
			} else if (txState.equals("6")) {
				statusText = "Transaccion Rechazada";
				status = PaymentTransactionStatus.REJECTED;
			} else if (txState.equals("104")) {
				statusText = "Error";
				status = PaymentTransactionStatus.ERROR;
			} else if (txState.equals("7")) {
				statusText = "Transaccion Pendiente";
				status = PaymentTransactionStatus.PROCESSING;
				tx.setEndDate(null);
			} else if (txState.equals("5")) {
				statusText = "Transaccion Expirada";
				status = PaymentTransactionStatus.EXPIRED;
			} else {
				status = PaymentTransactionStatus.UNKNOWN;
				statusText = response.get("mensaje");

				if (statusText == null || statusText.isEmpty()) {
					statusText = response.get("message");
				}

				if (statusText == null || statusText.isEmpty()) {
					statusText = "Desconocido";
				}
			}

			if (status == PaymentTransactionStatus.COMPLETED) {
				tx.setConfirmed(true);
			}

			tx.setStatus(status);
			tx.setStatusText(statusText);
			tx.setBank(response.get(RES_PSE_BANK));
			tx.setResponseCode(txState + "  -  " + response.get(RES_POL_RESPONSE_CODE));
			tx.setPaymentMethod(response.get(RES_LAP_PAYMENT_METHOD));
			tx.setReference(response.get(RES_REFERENCE_POL));
			tx.setReference2(response.get(RES_CUS));
			tx.setReference3(response.get(RES_PSE_REFERENCE1) + " " + response.get(RES_PSE_REFERENCE2) + " "
					+ response.get(RES_PSE_REFERENCE3));
			tx.setGatewayResponse(mapToString(response));
			service.saveTransaction(tx);
			return true;
		} else {
			return false;
		}
	}

	private String getResponseState(Map<String, String> response, ResponseType type) {
		String txState = null;
		if (type == ResponseType.RESPONSE) {
			txState = response.get(RES_TRANSACTION_STATE);
		} else if (type == ResponseType.CONFIRMATION) {
			txState = response.get(RES_STATE_POL);

			if (txState == null || txState.isEmpty()) {
				txState = response.get(RES_TRANSACTION_STATE);
			}
		}
		return txState;
	}

	public boolean isValidSignature(String apiKey, Map<String, String> response, ResponseType type) {
		// $ApiKey~$merchant_id~$referenceCode~$New_value~$currency~$transactionState

		String merchantId = response.get(MERCHANT_ID);
		String txuuid = locateTransactionId(response);
		String value = response.get(TX_VALUE);
		value = new BigDecimal(value).setScale(1, RoundingMode.HALF_EVEN).floatValue() + "";
		String currency = response.get(CURRENCY);
		String state = getResponseState(response, type);

		String responseSignature = response.get(SIGNATURE);
		logger.info("PayU ===> Validating Response Signature: " + responseSignature + " == MD5($ApiKey~" + merchantId
				+ "~" + txuuid + "~" + value + "~" + currency + "~" + state + ")");
		String computeSignature = md5(
				apiKey + "~" + merchantId + "~" + txuuid + "~" + value + "~" + currency + "~" + state);

		return computeSignature.equals(responseSignature);
	}

	public String generateSignature(String apiKey, Map<String, String> params) {

		String merchantId = params.get(MERCHANT_ID);
		String txuuid = locateTransactionId(params);
		String amount = params.get(AMOUNT);
		String currency = params.get(CURRENCY);

		if (apiKey == null) {
			throw new PaymentException("No API key provided for PayU Latam");
		}

		String rawSignature = apiKey + "~" + merchantId + "~" + txuuid + "~" + amount + "~" + currency;

		String signature = md5(rawSignature);

		return signature;
	}

}
