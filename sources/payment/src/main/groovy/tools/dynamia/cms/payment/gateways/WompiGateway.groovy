/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.payment.gateways

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.xml.internal.bind.v2.model.core.ID
import org.springframework.transaction.TransactionStatus
import tools.dynamia.cms.payment.*
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.util.DomainUtils
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.web.util.HttpUtils

@Service
class WompiGateway implements PaymentGateway {

    private static LoggingService LOGGER = new SLF4JLoggingService(WompiGateway)


    //settings
    public static final String PUBLIC_KEY = "public_key"
    public static final String CURRENCY = "currency"


    //response
    public static final String ID_RESPUESTA = "id"
    public static final String REFERENCE = "reference"
    public static final String STATUS = "status"
    public static final String CUSTOMER_EMAIL = "customer_email"
    public static final String AMOUNT_IN_CENTS = "amount_in_cents"
    public static final String PAYMENT_METHOD_TYPE = "payment_method_type"
    public static final String TEST = "test"


    //TOTAL


    @Override
    String getName() {
        return "Wompi"
    }

    @Override
    String getId() {
        return "wompi"
    }

    @Override
    String[] getRequiredParams() {
        return [PUBLIC_KEY, CURRENCY, TEST]
    }

    @Override
    String[] getResponseParams() {
        return [ID_RESPUESTA, REFERENCE, STATUS, CUSTOMER_EMAIL, AMOUNT_IN_CENTS, PAYMENT_METHOD_TYPE]
    }

    @Override
    PaymentTransaction newTransaction(PaymentGatewayAccount account, String baseURL) {
        if (account == null) {
            throw new PaymentException("No account for new transaction")
        }

        if (baseURL != null && baseURL.endsWith("//")) {
            baseURL = baseURL.replace("//", "/")
        }

        Map<String, String> config = account.configurationMap
        def tx = new PaymentTransaction(account.source)
        tx.paymentAccount = account

        tx.responseURL = baseURL + "payment/" + id + "/response"
        tx.confirmationURL = baseURL + "payment/" + id + "/confirmation"
        tx.status = PaymentTransactionStatus.NEW
        tx.test = config[TEST] == "true"
        return tx
    }

    @Override
    PaymentForm createForm(PaymentTransaction tx) {

        if (tx.paymentAccount == null) {
            throw new PaymentException("Cannot create payment form because transaction need an Account")
        }

        Map<String, String> config = tx.paymentAccount.configurationMap

        Map<String, String> data = new HashMap<>()
        data["public-key"] = config[PUBLIC_KEY]
        data["currency"] = config[CURRENCY]
        data["amount-in-cents"] = String.valueOf(tx.amount.longValue()) + "00"
        data["reference"] = tx.uuid
        data["redirect-url"] = tx.responseURL

        PaymentForm form = new PaymentForm()
        form.setHttpMethod("GET")
        form.setUrl("https://checkout.wompi.co/p/")
        form.addParams(data)

        return form
    }

    @Override
    boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {

        if (type == ResponseType.RESPONSE) {
            checkTransaction(tx, response)
        } else if (type == ResponseType.CONFIRMATION) {
            checkTransaction(tx, response)
        }

        if (tx.status == PaymentTransactionStatus.COMPLETED) {
            tx.confirmed = true
        }

        tx.save()
        return tx.confirmed
    }

    @Override
    String locateTransactionId(Map<String, String> response) {

        if (response.containsKey(REFERENCE)) {
            String uuid = response[REFERENCE]
            if (uuid != null && !uuid.empty) {
                return uuid
            }
        }


        String idRespuesta = response[ID_RESPUESTA]

        if (!idRespuesta) {
            return null
        }

        def account = DomainUtils.lookupCrudService().findSingle(PaymentGatewayAccount, QueryParameters.with("gatewayId", QueryConditions.eq(getId()))
                .add("enabled", true))
        if (!account) {
            return null;
        }
        def config = account.configurationMap

        String endpoint = "/v1/transactions/"
        String url = config[TEST] == "true" ? "https://sandbox.wompi.co" : "https://production.wompi.co"
        String urlVerificacion = url + endpoint + idRespuesta

        try {
            String jsonResponse = HttpUtils.executeHttpRequest(urlVerificacion)
            ObjectMapper mapper = new ObjectMapper()
            JsonNode node = mapper.reader().readTree(jsonResponse)
            if (node.get("data") != null) {
                JsonNode data = node.get("data")
                data.fields().forEachRemaining({ f ->
                    response[f.key] = f.value.asText()
                })

                String txUuid = response[REFERENCE]
                if (txUuid != null) {
                    return txUuid
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error location woompi transaction ID", e)
        }
        return null
    }


    private void checkTransaction(PaymentTransaction tx, Map<String, Object> respuesta) {
        if (!tx.confirmed) {

            def woompiStatus = respuesta[STATUS]
            def status = tx.status
            switch (woompiStatus) {
                case "APPROVED":
                    status = PaymentTransactionStatus.COMPLETED
                    break
                case "DECLINED":
                    status = PaymentTransactionStatus.REJECTED
                    break;
                case "VOIDED":
                    status = PaymentTransactionStatus.CANCELLED
                    break
                case "ERROR":
                    status = PaymentTransactionStatus.ERROR
                    break
            }

            tx.status = status
            if (respuesta["status_message"] != null && respuesta["status_message"] != "null") {
                tx.statusText = respuesta["status_message"]
            }
            tx.checkStatusText()
            tx.reference = respuesta[ID_RESPUESTA]
            tx.paymentMethod = respuesta[PAYMENT_METHOD_TYPE]
            tx.email = respuesta[CUSTOMER_EMAIL]
            tx.save()
        }
    }

    private static String get(Map<String, Object> respuesta, String name) {
        Object value = respuesta.get(name)
        if (value != null) {
            return value.toString().replace("\n", "")
        } else {
            return null
        }
    }

    boolean isValidSignature(PaymentTransaction tx, Map<String, String> response) {
        // hash('sha256',$p_cust_id_cliente.'^'.$p_key.'^'.$x_ref_payco.'^'.$x_transaction_id.'^'.$x_amount.'^'.$x_currency_code)

        Map<String, String> config = tx.paymentAccount.configurationMap

        String refPayco = config[X_REF_PAYCO]
        String custId = config[CUSTOMER_ID]
        String key = config[KEY]
        String txuuid = response[TRANSACTION_ID]
        String value = tx.amount.longValue()
        String currency = tx.currency


        String responseSignature = response.get(SIGNATURE)

        String computeSignature = PaymentUtils.sha256(custId + "^" + key + "^" + refPayco + "^" + txuuid + "^" + value + "^" + currency)

        return computeSignature == responseSignature
    }
}
