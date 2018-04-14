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
package tools.dynamia.cms.site.payment.gateways

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.site.payment.PaymentException
import tools.dynamia.cms.site.payment.PaymentForm
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.cms.site.payment.ResponseType
import tools.dynamia.cms.site.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.site.payment.domain.PaymentTransaction
import tools.dynamia.cms.site.payment.services.PaymentService
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
    PaymentTransaction newTransaction(String source, String baseURL) {
        PaymentTransaction tx = new PaymentTransaction(source)

        if (!baseURL.endsWith("/")) {
            baseURL += "/"
        }

        tx.setBaseURL(baseURL)
        tx.setResponseURL(baseURL + "payment/" + getId() + "/response")
        tx.setConfirmationURL(baseURL + "payment/" + getId() + "/confirmation")
        return tx
    }

    @Override
    PaymentForm createForm(PaymentTransaction tx) {
        Map<String, String> params = service.getGatewayConfigMap(this, tx.getSource())
        PaymentForm form = new PaymentForm()
        form.setHttpMethod("post")

        String sanboxMode = params.get(SANBOX_MODE)

        if ("true".equals(sanboxMode) || "1".equals(sanboxMode)) {
            form.setUrl(SANBOX_URL)
        } else {
            form.setUrl(URL)
        }

        if (tx.getCurrency() == null || tx.getCurrency().isEmpty()) {
            throw new PaymentException("No Currency supplied for Paypal")
        }

        form.addParam("cmd", "_xclick")
        form.addParam(BUSINESS, params.get(ACCOUNT_NAME))
        form.addParam(CUSTOM, tx.getUuid())
        form.addParam(INVOICE, tx.getDocument())

        form.addParam("charset", "utf-8")
        form.addParam("return", tx.getResponseURL())
        form.addParam("rm", "0")
        form.addParam("cancel_return", tx.getBaseURL())
        form.addParam("notify_url", tx.getConfirmationURL())

        form.addParam("currency_code", tx.getCurrency())
        form.addParam("item_name", tx.getDocument() + " - " + tx.getDescription())
        form.addParam("amount", tx.getAmount().toString())
        form.addParam("first_name", tx.getPayerFullname())
        form.addParam("last_name", tx.getPayerDocument())
        form.addParam("payer_email", tx.getEmail())

        return form
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    boolean processResponse(PaymentTransaction tx, Map<String, String> response, ResponseType type) {

        logger.info("Procesing response for " + tx.getUuid())
        logger.info("Response Params:" + response)

        Map<String, String> params = service.getGatewayConfigMap(this, tx.getSource())

        String accountName = params.get(ACCOUNT_NAME)

        String txId = response.get(TX)
        String amount = response.get(AMT)
        String currency = response.get(CC)
        String status = response.get(ST)
        String uuid = response.get(CUSTOM)
        String invoice = response.get(INVOICE)
        String business = response.get(BUSINESS)

        if (business.equals(accountName) && amount.equals(tx.getAmount().toString())
                && currency.equals(tx.getCurrency())) {
            tx.setReference(txId)
            tx.setReference2(invoice)
            tx.setReference3(uuid)
            tx.setStatus(PaymentTransactionStatus.COMPLETED)
            tx.setStatusText("Payment completed successfully")
        } else {
            tx.setStatus(PaymentTransactionStatus.ERROR)
            tx.setStatusText(status)
        }

        logger.info("Response proceesed. Status: " + tx.getStatus() + " - " + tx.getStatusText())

        return true

    }

}
