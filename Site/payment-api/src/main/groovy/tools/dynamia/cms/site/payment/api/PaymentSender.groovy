package tools.dynamia.cms.site.payment.api

import tools.dynamia.cms.site.payment.api.dto.ManualPaymentDTO
import tools.dynamia.cms.site.payment.api.dto.PaymentDTO

public interface PaymentSender {

    List<Response> sendManualPayments(List<ManualPaymentDTO> payments, Map<String, String> params);

    List<Response> sendPayments(List<PaymentDTO> payments, Map<String, String> params);
}
