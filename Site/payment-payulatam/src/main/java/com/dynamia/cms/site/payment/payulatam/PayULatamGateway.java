package com.dynamia.cms.site.payment.payulatam;

import static com.dynamia.cms.site.payment.PaymentUtils.eq;
import static com.dynamia.cms.site.payment.PaymentUtils.mapToString;
import static com.dynamia.cms.site.payment.PaymentUtils.md5;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dynamia.cms.site.payment.PaymentForm;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.domain.services.CrudService;

@Service
public class PayULatamGateway implements PaymentGateway {

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
	private static final String TEST_URL = "testUrl";
	private static final String PRODUCTION_URL = "productionUrl";
	@Autowired
	private PaymentService service;
	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "PayU Latam";
	}

	@Override
	public String getId() {
		return "payulatam";
	}

	@Override
	public String getTransactionLocator() {
		return REFERENCE_CODE;
	}

	@Override
	public String[] getRequiredParams() {
		return new String[] { API_KEY, ACCOUNT_ID, MERCHANT_ID, TEST, TEST_URL, PRODUCTION_URL };
	}

	@Override
	public PaymentTransaction newTransaction(String source) {
		PaymentTransaction tx = new PaymentTransaction(source);

		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());
		baseUrl = baseUrl.replace(":80/", "/");
		tx.setResponseURL(baseUrl + "payment/" + getId() + "/response");
		tx.setConfirmationURL(baseUrl + "payment/" + getId() + "/confirmation");
		return tx;
	}

	@Override
	public PaymentForm createForm(PaymentTransaction tx) {

		PaymentForm form = new PaymentForm();
		form.setHttpMethod("post");
		Map<String, String> params = service.getGatewayConfigMap(this, tx.getSource());

		if (tx.isTest()) {
			form.setUrl(params.get(TEST_URL));
		} else {
			form.setUrl(params.get(PRODUCTION_URL));
		}

		form.addParam(MERCHANT_ID, params.get(MERCHANT_ID));
		form.addParam(ACCOUNT_ID, params.get(ACCOUNT_ID));
		form.addParam(REFERENCE_CODE, tx.getUuid());
		form.addParam(AMOUNT, String.valueOf(tx.getAmount().intValue()));
		form.addParam(TAX, String.valueOf(tx.getTaxes().intValue()));
		form.addParam(TAX_RETURN_BASE, String.valueOf(tx.getTaxesBase().intValue()));
		form.addParam(CURRENCY, tx.getCurrency());
		form.addParam(TEST, tx.isTest() ? "1" : "0");
		form.addParam(BUYER_EMAIL, tx.getEmail());
		form.addParam(RESPONSE_URL, tx.getResponseURL());
		form.addParam(CONFIRMATION_URL, tx.getConfirmationURL());

		tx.setSignature(generateSignature(form.getParamaters()));
		form.addParam(SIGNATURE, tx.getSignature());
		form.addParam(DESCRIPTION, tx.getDescription());

		return form;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean commit(PaymentTransaction tx, Map<String, String> response) {
		String responseSignature = response.get(SIGNATURE);

		if (tx.getEndDate() == null) {
			tx.setEndDate(new Date());
		}

		if (!tx.isValidSignature(responseSignature)) {
			tx.setStatus(PaymentTransactionStatus.ERROR);
			tx.setStatusText("Firma digital invalida");
			crudService.save(tx);
			return false;
		}

		String statusText = "";
		PaymentTransactionStatus status = tx.getStatus();
		if (eq(response, "polTransactionState", "6") && eq(response, "polResponseCode", "5")) {
			statusText = "Transacción fallida";
			status = PaymentTransactionStatus.FAILED;
		} else if (eq(response, "polTransactionState", "6") && eq(response, "polResponseCode", "4")) {
			statusText = "Transacción rechazada";
			status = PaymentTransactionStatus.REJECTED;
		} else if (eq(response, "polTransactionState", "12") && eq(response, "polResponseCode", "9994")) {
			statusText = "Pendiente, Por favor revisar si el débito fue realizado en el Banco";
			status = PaymentTransactionStatus.PROCESSING;
			tx.setEndDate(null);
		} else if (eq(response, "polTransactionState", "4") && eq(response, "polResponseCode", "1")) {
			statusText = "Transacción aprobada";
			status = PaymentTransactionStatus.COMPLETED;
		} else {
			status = PaymentTransactionStatus.CANCELLED;
			statusText = response.get("mensaje");
			if (statusText == null || statusText.isEmpty()) {
				statusText = response.get("message");
			}
		}

		tx.setStatus(status);
		tx.setStatusText(statusText);
		tx.setBank(response.get("pseBank"));
		tx.setResponseCode(response.get("polTransactionState") + "  -  " + response.get("polResponseCode"));
		tx.setPaymentMethod(response.get("lapPaymentMethod"));
		tx.setReference(response.get("reference_pol"));
		tx.setReference2(response.get("cus"));
		tx.setReference3(response.get("pseReference1") + " " + response.get("pseReference2") + " " + response.get("pseReference3"));
		tx.setGatewayResponse(mapToString(response));
		crudService.save(tx);
		return true;
	}

	private String generateSignature(Map<String, String> params) {
		String apiKey = params.get(API_KEY);
		String merchantId = params.get(MERCHANT_ID);
		String referenceCode = params.get(REFERENCE_CODE);
		String amount = params.get(AMOUNT);
		String currency = params.get(CURRENCY);

		String rawSignature = apiKey + "~" + merchantId + "~" + referenceCode + "~" + amount + "~" + currency;
		String signature = md5(rawSignature);

		return signature;
	}

}
