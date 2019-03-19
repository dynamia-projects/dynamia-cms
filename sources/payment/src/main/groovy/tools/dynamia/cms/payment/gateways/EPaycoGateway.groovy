/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

import org.springframework.stereotype.Service
import tools.dynamia.cms.payment.PaymentException
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.ResponseType
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction

//@Service
class EPaycoGateway implements PaymentGateway {

    public static final String PREFIX = "data-epayco-"

    //settings
    public static final String KEY = "key"
    public static final String CURRENCY = "currency"
    public static final String EXTERNAL = "external"
    public static final String TEST = "test" // 1 / 0
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
    public static final String REF_PAYCO = "x_ref_payco"
    public static final String TRANSACTION_ID = "x_transaction_id"
    public static final String AMOUNT = "x_amount"
    public static final String CURRENCY_CODE = "x_currency_code"

    public static final String DESCRIPTION = "x_description"
    public static final String ID_INVOCE = "x_id_invoice"
    public static final String CUSTOMER_NAME = "x_customer_name"
    public static final String CUSTOMER_LAST_NAME = "x_customer_lastname"
    public static final String CUSTOMER_EMAIL = "x_customer_email"
    public static final String CUSTOMER_IP = "x_customer_ip"

    public static final String AMOUNT_BASE = "x_amount_base" //BASE
    public static final String TAX = "x_tax" //IVA
    public static final String AMOUNT_TOTAL = "x_amount_ok" //TOTAL


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
        return [KEY, CURRENCY, EXTERNAL, TEST, AUTOCLICK]
    }

    @Override
    String[] getResponseParams() {
        return [
                COD_RESPONSE, RESPONSE, RESPONSE_REASON, APPROVAL_CODE, FRANCHISE, SIGNATURE, CUST_ID_CLIENTE, REF_PAYCO,
                TRANSACTION_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION, ID_INVOCE, CUSTOMER_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL,
                CUSTOMER_IP, AMOUNT_BASE, TAX, AMOUNT_TOTAL
        ]
    }

    @Override
    PaymentTransaction newTransaction(PaymentGatewayAccount account, String baseURL) {
        if (account == null) {
            throw new PaymentException("No account for new transaction")
        }

        def tx = new PaymentTransaction(account.source)
        tx.paymentAccount = account

        tx.responseURL = baseURL + "payment/" + id + "/response"
        tx.confirmationURL = baseURL + "payment/" + id + "/confirmation"
        return tx
    }

    @Override
    PaymentForm createForm(PaymentTransaction tx) {

        if (tx.paymentAccount == null) {
            throw new PaymentException("Cannot create payment form because transaction need an Account")
        }
        Map<String, String> params = tx.paymentAccount.configurationMap
        PaymentForm form = new PaymentForm()

        return null
    }

    @Override
    boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {
        return false
    }

    @Override
    String locateTransactionId(Map<String, String> response) {
        return response[ID_INVOCE]
    }

    String generateSignature(PaymentTransaction tx, Map<String, String> response) {

        String custIdCliente = response[CUST_ID_CLIENTE]
        String key = tx.paymentAccount.configurationMap[KEY]
        String ref = response[REF_PAYCO]
        String transaction = response[TRANSACTION_ID]
        String amount = response[AMOUNT]
        String currency = response[CURRENCY_CODE]

    }
}
