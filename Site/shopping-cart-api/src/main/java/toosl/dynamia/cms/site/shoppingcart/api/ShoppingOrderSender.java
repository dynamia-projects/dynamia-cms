package toosl.dynamia.cms.site.shoppingcart.api;

import java.util.List;
import java.util.Map;

import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderDTO;

public interface ShoppingOrderSender {

	public String sendOrder(ShoppingOrderDTO dto, Map<String, String> params);

	Map<String, String> sendOrders(List<ShoppingOrderDTO> dtos, Map<String, String> params);
}
