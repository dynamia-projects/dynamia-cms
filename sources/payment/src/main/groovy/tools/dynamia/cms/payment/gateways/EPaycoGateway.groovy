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
import tools.dynamia.cms.payment.*
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.web.util.HttpUtils

@Service
class EPaycoGateway implements PaymentGateway {

    private static LoggingService LOGGER = new SLF4JLoggingService(EPaycoGateway)

    public static final String PREFIX = "data-epayco-"

    //settings
    public static final String KEY = "webcheckoutKey"
    public static final String CUSTOMER_ID = "customerId"
    public static final String PRIVATE_KEY = "private_key"
    public static final String PUBLIC_KEY = "public_key"
    public static final String CURRENCY = "currency"
    public static final String EXTERNAL = "external" // true / false
    public static final String TEST = "test" // true / fase
    public static final String AUTOCLICK = "autoclick" //true / false

    //checkout
    public static final String CHK_INVOICE = PREFIX + "invoice"
    public static final String CHK_AMOUNT = PREFIX + "amount"
    public static final String CHK_TAX = PREFIX + "tax"
    public static final String CHK_TAX_BASE = PREFIX + "tax-base"
    public static final String CHK_NAME = PREFIX + "name"
    public static final String CHK_DESCRIPTION = PREFIX + "description"
    public static final String CHK_RESPONSE = PREFIX + "response"
    public static final String CHK_CONFIRMATION = PREFIX + "confirmation"


    //response
    public static final String COD_RESPONSE = "x_cod_response"
    public static final String RESPONSE = "x_response"
    public static final String RESPONSE_REASON = "x_response_reason_text"
    public static final String APPROVAL_CODE = "x_approval_code"
    public static final String FRANCHISE = "x_franchise"
    public static final String SIGNATURE = "x_signature"
    public static final String CUST_ID_CLIENTE = "x_cust_id_cliente"
    public static final String X_REF_PAYCO = "x_ref_payco"
    public static final String REF_PAYCO = "ref_payco"
    public static final String TRANSACTION_ID = "x_transaction_id"
    public static final String AMOUNT = "x_amount"
    public static final String CURRENCY_CODE = "x_currency_code"
    public static final String PAYMENT_METHOD = "x_type_payment"

    public static final String DESCRIPTION = "x_description"
    public static final String ID_INVOCE = "x_id_invoice"
    public static final String CUSTOMER_NAME = "x_customer_name"
    public static final String CUSTOMER_LAST_NAME = "x_customer_lastname"
    public static final String CUSTOMER_EMAIL = "x_customer_email"


    public static final String AMOUNT_BASE = "x_amount_base" //BASE
    public static final String TAX = "x_tax" //IVA
    public static final String AMOUNT_TOTAL = "x_amount_ok"
    public static final String TRANSACTION_STATE = "x_transaction_state"
    public static final String BANK_NAME = "x_bank_name"
    public static final String CUSTOMER_IP = "x_customer_ip"
    public static final String X_EXTRA1 = "x_extra1"
    //TOTAL


    @Override
    String getName() {
        return "ePayCO"
    }

    @Override
    String getId() {
        return "epayco"
    }

    @Override
    String[] getRequiredParams() {
        return [KEY, CUSTOMER_ID, PRIVATE_KEY, PUBLIC_KEY, CURRENCY, EXTERNAL, TEST, AUTOCLICK]
    }

    @Override
    String[] getResponseParams() {
        return [
                COD_RESPONSE, RESPONSE, RESPONSE_REASON, APPROVAL_CODE, FRANCHISE, SIGNATURE, CUST_ID_CLIENTE, X_REF_PAYCO, REF_PAYCO,
                TRANSACTION_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION, ID_INVOCE, CUSTOMER_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL,
                CUSTOMER_IP, AMOUNT_BASE, TAX, AMOUNT_TOTAL, BANK_NAME, PAYMENT_METHOD, TRANSACTION_STATE
        ]
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
        tx.test = config[TEST] == "true"
        tx.status = PaymentTransactionStatus.NEW
        return tx
    }

    @Override
    PaymentForm createForm(PaymentTransaction tx) {

        if (tx.paymentAccount == null) {
            throw new PaymentException("Cannot create payment form because transaction need an Account")
        }

        Map<String, String> config = tx.paymentAccount.configurationMap
        String key = config[PUBLIC_KEY]


        Map<String, Object> data = new HashMap<>()
        data.put("name", tx.payerFullname)
        data.put("description", tx.description)
        data.put("invoice", tx.uuid)
        data.put("currency", config[CURRENCY])
        data.put("amount", tx.amount.longValue())
        data.put("tax_base", tx.taxesBase.longValue())
        data.put("tax", tx.taxes.longValue())
        data.put("test", tx.test)
        data.put("lang", "es")
        data.put("external", "true" == config[EXTERNAL])
        data.put("confirmation", tx.confirmationURL)
        data.put("response", tx.responseURL)
        data.put("name_billing", tx.payerFullname)
        data.put("address_billing", tx.shippingAddress)
        data.put("type_doc_billing", "cc")
        data.put("number_doc_billing", tx.payerDocument)
        data.put("mobilephone_billing", tx.payerMobileNumber)
        data.put("country", "co")
        data.put("key", key)


        StringBuilder json = new StringBuilder()
        json.append("{\n")
        data.each {
            def v = it.value
            def k = it.key
            String value = v != null ? v.toString() : ""
            json.append(k).append(": \"").append(value).append("\", \n")
        }
        json.append("extra1: \"").append(tx.uuid).append("\" \n")
        json.append("}")

        PaymentForm form = new PaymentForm()
        form.scriptResources = ["https://checkout.epayco.co/checkout.js"]
        form.scriptMode = true
        form.scriptSrc =
                """
                var handler = ePayco.checkout.configure({
                       key: '$key',
                       test: $tx.test
                }) 

                handler.open(${json.toString()});
            """

        return form
    }

    @Override
    boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {

        if (type == ResponseType.RESPONSE) {
            checkTransaction(tx, response)
        } else if (type == ResponseType.CONFIRMATION) {
            checkTransaction(tx, response)
        }

        if(tx.status==PaymentTransactionStatus.COMPLETED){
            tx.confirmed=true
        }

        tx.save()
        return tx.confirmed
    }

    @Override
    String locateTransactionId(Map<String, String> response) {

        if (response.containsKey(X_EXTRA1)) {
            String uuid = response[X_EXTRA1]
            if (uuid != null && !uuid.empty) {
                return uuid
            }
        }


        String refPayco = response[REF_PAYCO]

        if (!refPayco) {
            return null
        }

        String urlVerificacion = "https://secure.epayco.co/validation/v1/reference/" + refPayco


        try {
            String jsonResponse = HttpUtils.executeHttpRequest(urlVerificacion)
            ObjectMapper mapper = new ObjectMapper()
            JsonNode node = mapper.reader().readTree(jsonResponse)
            if (node.get("success") != null && node.get("success").booleanValue()) {
                JsonNode data = node.get("data")
                data.fields().forEachRemaining({ f ->
                    response[f.key] = f.value.asText()
                })

                String txUuid = data.get(X_EXTRA1).textValue()
                if (txUuid != null) {
                    return txUuid
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error location epayco transaction ID", e)
        }
        return null
    }


    private void checkTransaction(PaymentTransaction tx, Map<String, Object> respuesta) {
        if (!tx.confirmed) {

            if (!isValidSignature(tx, respuesta)) {
                //tx.status = PaymentTransactionStatus.ERROR
               // tx.statusText = "Firma de Transaccion Invalida"
              //  return
            }

            Map<String, String> config = tx.paymentAccount.configurationMap

            def status = tx.status
            def refPayco = config[X_REF_PAYCO]

            def code = respuesta[COD_RESPONSE]
            def state = respuesta[TRANSACTION_STATE]

            if ("1" == code && "Aceptada" == state) {
                status = PaymentTransactionStatus.COMPLETED
            } else if ("2" == code && "Rechazada" == state) {
                status = PaymentTransactionStatus.REJECTED
            } else if ("3" == code && "Pendiente" == state) {
                status = PaymentTransactionStatus.PROCESSING
            } else if ("4" == code && "Fallida" == state) {
                status = PaymentTransactionStatus.FAILED
            } else if ("9" == code && "Expirada" == state) {
                status = PaymentTransactionStatus.EXPIRED
            } else if ("11" == code && "Cancelada" == state) {
                status = PaymentTransactionStatus.CANCELLED
            }

            tx.status = status
            tx.statusText = state

            tx.reference = refPayco
            tx.paymentMethod = get(respuesta, PAYMENT_METHOD)
            tx.bank = get(respuesta, BANK_NAME)
            tx.clientIP = get(respuesta, CUSTOMER_IP)


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
