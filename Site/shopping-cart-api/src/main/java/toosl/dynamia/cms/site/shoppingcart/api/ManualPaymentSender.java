package toosl.dynamia.cms.site.shoppingcart.api;

import java.util.List;
import java.util.Map;

import toosl.dynamia.cms.site.shoppingcart.dto.ManualPaymentDTO;

public interface ManualPaymentSender {

	public List<Response> sendPayments(List<ManualPaymentDTO> payments, Map<String, String> params);
}
