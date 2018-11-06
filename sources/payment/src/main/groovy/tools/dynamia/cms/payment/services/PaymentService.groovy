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
package tools.dynamia.cms.payment.services

import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.api.PaymentSource
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentGatewayConfig
import tools.dynamia.cms.payment.domain.PaymentTransaction

interface PaymentService {

    /**
     * @param gatewayId
     * @return
     */
    PaymentGatewayAccount getDefaultAccount(String source)

    abstract PaymentGateway findGateway(String gatewayId)

    /**
     * @param gateway
     * @param response
     * @return
     */
    abstract PaymentTransaction findTransaction(PaymentGateway gateway, Map<String, String> response)

    /**
     * @return
     */
    abstract PaymentGateway getDefaultGateway()

    /**
     * @param tx
     */
    abstract void saveTransaction(PaymentTransaction tx)

    /**
     * @param payment
     */
    abstract void register(ManualPayment payment)

    /**
     * @param source
     * @param payerCode
     * @return
     */
    List<ManualPayment> findManualPaymentsByPayerCode(String source, String payerCode)

    /**
     * @param source
     * @param payerId
     * @return
     */
    List<ManualPayment> findManualPaymentsByPayerId(String source, String payerId)

    /**
     * @param source
     * @param registratorCode
     * @param payerCode
     * @return
     */
    List<ManualPayment> findManualPayments(String source, String registratorCode, String payerCode)

    /**
     * @param source
     * @param serviceUrl
     * @param params
     */
    void sendManualPayments(String source, String serviceUrl, Map<String, String> params)

    /**
     * @param source
     * @param serviceUrl
     * @param params
     */
    void sendPayments(String source, String serviceUrl, Map<String, String> params)

    PaymentSource findPaymentSource(Object request)

    def findAccount(String id);
}