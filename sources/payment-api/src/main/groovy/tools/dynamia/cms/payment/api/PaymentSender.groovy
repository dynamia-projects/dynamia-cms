package tools.dynamia.cms.payment.api

interface PaymentSender {

    List<Response> sendManualPayments(List<tools.dynamia.cms.payment.api.dto.ManualPaymentDTO> payments, Map<String, String> params)

    List<Response> sendPayments(List<tools.dynamia.cms.payment.api.dto.PaymentDTO> payments, Map<String, String> params)
}
