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

    /**
     * Find a payment gateway account by its UUID
     * @param uuid
     * @return
     */
    PaymentGatewayAccount findAccount(String uuid);
}