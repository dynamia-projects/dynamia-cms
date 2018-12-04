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
        return crudService().findSingle(PaymentGatewayAccount, "source", QueryConditions.eq(source))
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
        return crudService().findSingle(PaymentGatewayAccount,"uuid",QueryConditions.eq(uuid))
    }
}
