package tools.dynamia.cms.site.payment.api;

import java.util.List;
import java.util.Map;

import tools.dynamia.cms.site.payment.api.dto.ManualPaymentDTO;
import tools.dynamia.cms.site.payment.api.dto.PaymentDTO;

public interface PaymentSender {

	public List<Response> sendManualPayments(List<ManualPaymentDTO> payments, Map<String, String> params);
	
	public List<Response> sendPayments(List<PaymentDTO> payments, Map<String, String> params);
}
