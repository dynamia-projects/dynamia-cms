package toosl.dynamia.cms.site.shoppingcart.api

import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderDTO

public interface ShoppingOrderSender {

	public Response sendOrder(ShoppingOrderDTO dto, Map<String, String> params);

	List<Response> sendOrders(List<ShoppingOrderDTO> dtos, Map<String, String> params);
}
