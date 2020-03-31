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
package tools.dynamia.cms.payment.services.impl

import groovy.transform.CompileStatic
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.payment.PaymentException
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.cms.payment.api.*
import tools.dynamia.cms.payment.api.dto.ManualPaymentDTO
import tools.dynamia.cms.payment.api.dto.PaymentDTO
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.domain.PaymentGatewayAccount
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.query.ApplicationParameters
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.AbstractService
import tools.dynamia.domain.util.DomainUtils
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Service
import tools.dynamia.web.util.HttpRemotingServiceClient

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
@CompileStatic
class PaymentServiceImpl extends AbstractService implements PaymentService {

    private static final String LAST_MANUAL_PAYMENT_NUMBER = "lastManualPaymentNumber-"

    @PersistenceContext
    private EntityManager em


    @Override
    PaymentGatewayAccount getDefaultAccount(String source) {
        return crudService().findSingle(PaymentGatewayAccount, QueryParameters.with("source", QueryConditions.eq(source))
                .add("enabled", true))
    }

    List<PaymentGatewayAccount> findAccounts(String source) {
        return crudService().find(PaymentGatewayAccount, "source", QueryConditions.eq(source))
    }

    List<PaymentGatewayAccount> findAccounts(PaymentGateway gateway, String source) {
        return crudService().find(PaymentGatewayAccount, QueryParameters.with("source", QueryConditions.eq(source))
                .add("gatewayId", QueryConditions.eq(gateway.id)))
    }

    @Override
    PaymentGateway findGateway(String gatewayId) {
        for (PaymentGateway gateway : Containers.get().findObjects(PaymentGateway.class)) {
            if (gateway.id == gatewayId) {
                return gateway
            }
        }
        throw new PaymentException("PaymentGateway with id [" + gatewayId + "] not found")
    }

    @Override
    PaymentTransaction findTransaction(PaymentGateway gateway, Map<String, String> response) {
        String uuid = gateway.locateTransactionId(response)
        if (uuid == null) {
            throw new PaymentException("No UUID for transaction found " + gateway.id)
        }

        PaymentTransaction tx = crudService().findSingle(PaymentTransaction.class, QueryParameters
                .with("uuid", QueryConditions.eq(uuid)).add("gatewayId", QueryConditions.eq(gateway.id)))

        if (tx == null) {
            throw new PaymentException("No transaction found for gateway " + gateway.id + " uuid: " + uuid)
        }
        tx.paymentAccount.configurationMap
        return tx

    }

    @Override
    PaymentGateway getDefaultGateway() {
        Collection<PaymentGateway> gateways = Containers.get().findObjects(PaymentGateway.class)
        for (PaymentGateway paymentGateway : gateways) {
            return paymentGateway
        }

        return null
    }

    @Override
    void saveTransaction(PaymentTransaction tx) {
        crudService().save(tx)

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void register(ManualPayment payment) {
        if (payment.number == null && payment.source != null) {
            String paramName = LAST_MANUAL_PAYMENT_NUMBER + payment.source
            long lastNumber = Long.parseLong(ApplicationParameters.get().getValue(paramName, "0"))
            lastNumber++

            ApplicationParameters.get().setParameter(paramName, lastNumber)

            String number = DomainUtils.formatNumberWithZeroes(lastNumber, 10000)
            payment.number = number
        }
        crudService().save(payment)
    }

    /**
     * @param source
     * @param registratorCode
     * @param payerCode
     * @return
     */
    @Override
    List<ManualPayment> findManualPayments(String source, String registratorCode, String payerCode) {
        return crudService().find(ManualPayment.class,
                QueryParameters.with("source", source).add("registratorCode", registratorCode)
                        .add("payerCode", payerCode).setAutocreateSearcheableStrings(false))

    }

    @Override
    List<ManualPayment> findManualPaymentsByPayerId(String source, String payerId) {
        return crudService().find(ManualPayment.class,
                QueryParameters.with("source", source).add("payerId", payerId).setAutocreateSearcheableStrings(false))

    }

    /**
     * @param source
     * @param payerCode
     * @return
     */
    @Override
    List<ManualPayment> findManualPaymentsByPayerCode(String source, String payerCode) {
        return crudService().find(ManualPayment.class, QueryParameters.with("source", source).add("payerCode", payerCode)
                .setAutocreateSearcheableStrings(false))

    }

    @Override
    void sendManualPayments(String source, String serviceUrl, Map<String, String> params) {
        try {
            log("Sending all manual Payments")

            if (serviceUrl != null && !serviceUrl.empty) {
                PaymentSender sender = HttpRemotingServiceClient.build(PaymentSender.class)
                        .setServiceURL(serviceUrl)
                        .getProxy()

                crudService().executeWithinTransaction {

                    List<ManualPayment> payments = crudService().find(ManualPayment.class, QueryParameters
                            .with("sended", false).add("source", source).setAutocreateSearcheableStrings(false))
                    log("Sending " + payments.size() + " manual payments for source " + source)

                    sendPayments(sender, payments, params)
                }

            }
            log("Manual payments Sended")
        } catch (Exception e) {
            log("Error sending manual payments. Source: " + source + ", Service URL: " + serviceUrl, e)
        }
    }

    @Override
    void sendPayments(String source, String serviceUrl, Map<String, String> params) {
        try {
            log("Sending all auto Payments")

            if (serviceUrl != null && !serviceUrl.empty) {
                PaymentSender sender = HttpRemotingServiceClient.build(PaymentSender.class)
                        .setServiceURL(serviceUrl)
                        .getProxy()

                crudService().executeWithinTransaction {

                    List<PaymentTransaction> payments = crudService().find(PaymentTransaction.class, QueryParameters
                            .with("sended", false).add("source", source)
                            .add("status", PaymentTransactionStatus.COMPLETED)
                            .add("confirmed", true)
                            .add("test", false)
                            .setAutocreateSearcheableStrings(false))
                    log("Sending " + payments.size() + " auto payments for source " + source)

                    sendPaymentsTransaction(sender, payments, params)
                }

            }
            log("Automatic payments Sended")
        } catch (Exception e) {
            log("Error sending auto payments. Source: " + source + ", Service URL: " + serviceUrl, e)
        }

    }

    void sendPayments(PaymentSender sender, List<ManualPayment> payments, Map<String, String> params) {
        List<ManualPaymentDTO> dtos = payments.collect { createDTO(it) }

        if (!dtos.empty) {
            log("Calling Payment Sender " + sender)
            try {
                List<Response> response = sender.sendManualPayments(dtos, params)

                if (response != null) {
                    log("Sending response recieved. " + response.size())
                    for (ManualPayment payment : payments) {
                        Response resp = Response.find(response, payment.number)
                        if (resp != null) {
                            if (resp.error) {
                                payment.errorCode = resp.errorCode
                                payment.errorMessage = resp.errorMessage
                            } else {
                                payment.errorCode = null
                                payment.errorMessage = null
                                payment.externalRef = resp.content
                                payment.sended = true
                            }
                        }
                        crudService().update(payment)
                        log("==> Updating payment " + payment.number + " ==> " + resp)
                    }
                }
            } catch (PaymentSenderException e) {
                log("Error", e)
            }
        }
    }

    private void sendPaymentsTransaction(PaymentSender sender, List<PaymentTransaction> payments, Map<String, String> params) {
        List<PaymentDTO> dtos = payments.collect { createDTO(it) }

        if (!dtos.empty) {
            log("Calling Payment Sender " + sender)
            try {
                List<Response> response = sender.sendPayments(dtos, params)

                if (response != null) {
                    log("Sending response recieved. " + response.size())
                    for (PaymentTransaction payment : payments) {
                        Response resp = Response.find(response, payment.uuid)
                        if (resp != null) {
                            if (resp.error) {
                                payment.errorCode = resp.errorCode
                                payment.errorMessage = resp.errorMessage
                            } else {
                                payment.errorCode = null
                                payment.errorMessage = null
                                payment.externalRef = resp.content
                                payment.sended = true
                            }
                        }
                        crudService().update(payment)
                        log("==> Updating payment " + payment.uuid + " ==> " + resp)
                    }
                }
            } catch (PaymentSenderException e) {
                log("Error", e)
            }
        }
    }

    private ManualPaymentDTO createDTO(ManualPayment ord) {
        ManualPaymentDTO dto = new ManualPaymentDTO()
        BeanUtils.setupBean(dto, ord)
        return dto
    }

    private PaymentDTO createDTO(PaymentTransaction ord) {
        PaymentDTO dto = new PaymentDTO()
        BeanUtils.setupBean(dto, ord)
        return dto
    }

    @Override
    PaymentSource findPaymentSource(Object request) {
        PaymentSourceProvider provider = Containers.get().findObject(PaymentSourceProvider.class)
        if (provider != null) {
            return provider.findSource(request)
        } else {
            return null
        }
    }

    @Override
    PaymentGatewayAccount findAccount(String uuid) {
        return crudService().findSingle(PaymentGatewayAccount, "uuid", QueryConditions.eq(uuid))
    }
}
