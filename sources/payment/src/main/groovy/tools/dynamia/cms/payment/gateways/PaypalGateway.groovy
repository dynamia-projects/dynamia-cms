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

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.payment.PaymentException
import tools.dynamia.cms.payment.PaymentForm
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.ResponseType
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService

@Service
class PaypalGateway implements PaymentGateway {

    private static final String SANBOX_MODE = "sanboxMode"

    private static final String ST = "st"

    private static final String CC = "cc"

    private static final String AMT = "amt"

    private static final String TX = "tx"

    private static final String BUSINESS = "business"

    private static final String INVOICE = "invoice"

    private static final String CUSTOM = "custom"

    private static final String ACCOUNT_NAME = "accountName"

    private static final String URL = "https://www.paypal.com/cgi-bin/webscr"

    private static final String SANBOX_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr"

    private PaymentService service

    private LoggingService logger = new SLF4JLoggingService(PaypalGateway.class, "[PAYPAL] ")

    @Autowired
    PaypalGateway(PaymentService service) {
        super()
        this.service = service

    }

    @Override
    String getName() {
        return "Paypal"
    }

    @Override
    String getId() {
        return "paypal"
    }

    @Override
    String locateTransactionId(Map<String, String> response) {
        String uuid = null
        if (response != null) {
            uuid = response.get(CUSTOM)
        }

        return uuid
    }

    @Override
    String[] getRequiredParams() {
        return [ACCOUNT_NAME, SANBOX_MODE]
    }

    @Override
    String[] getResponseParams() {
        return [CUSTOM, INVOICE, TX, AMT, CC, ST, BUSINESS]
    }

    @Override
    PaymentTransaction newTransaction(PaymentGatewayAccount account, String baseURL) {
        PaymentTransaction tx = new PaymentTransaction(account.source)
        tx.paymentAccount = account
        if (!baseURL.endsWith("/")) {
            baseURL += "/"
        }

        tx.baseURL = baseURL
        tx.responseURL = baseURL + "payment/" + id + "/response"
        tx.confirmationURL = baseURL + "payment/" + id + "/confirmation"
        return tx
    }

    @Override
    PaymentForm createForm(PaymentTransaction tx) {
        Map<String, String> params = tx.paymentAccount.configurationMap
        PaymentForm form = new PaymentForm()
        form.httpMethod = "post"

        String sanboxMode = params.get(SANBOX_MODE)

        if ("true" == sanboxMode || "1" == sanboxMode) {
            form.url = SANBOX_URL
        } else {
            form.setUrl(URL)
        }

        if (tx.currency == null || tx.currency.empty) {
            throw new PaymentException("No Currency supplied for Paypal")
        }

        form.addParam("cmd", "_xclick")
        form.addParam(BUSINESS, params.get(ACCOUNT_NAME))
        form.addParam(CUSTOM, tx.uuid)
        form.addParam(INVOICE, tx.document)

        form.addParam("charset", "utf-8")
        form.addParam("return", tx.responseURL)
        form.addParam("rm", "0")
        form.addParam("cancel_return", tx.baseURL)
        form.addParam("notify_url", tx.confirmationURL)

        form.addParam("currency_code", tx.currency)
        form.addParam("item_name", tx.document + " - " + tx.description)
        form.addParam("amount", tx.amount.toString())
        form.addParam("first_name", tx.payerFullname)
        form.addParam("last_name", tx.payerDocument)
        form.addParam("payer_email", tx.email)

        return form
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {

        logger.info("Procesing response for " + tx.uuid)
        logger.info("Response Params:" + response)

        Map<String, String> params = service.getGatewayConfigMap(this, tx.source)

        String accountName = params.get(ACCOUNT_NAME)

        String txId = response.get(TX)
        String amount = response.get(AMT)
        String currency = response.get(CC)
        String status = response.get(ST)
        String uuid = response.get(CUSTOM)
        String invoice = response.get(INVOICE)
        String business = response.get(BUSINESS)

        if (business == accountName && amount == tx.amount.toString()
                && currency == tx.currency) {
            tx.reference = txId
            tx.reference2 = invoice
            tx.reference3 = uuid
            tx.status = PaymentTransactionStatus.COMPLETED
            tx.statusText = "Payment completed successfully"
        } else {
            tx.status = PaymentTransactionStatus.ERROR
            tx.statusText = status
        }

        logger.info("Response proceesed. Status: " + tx.status + " - " + tx.statusText)

        return true

    }

}
