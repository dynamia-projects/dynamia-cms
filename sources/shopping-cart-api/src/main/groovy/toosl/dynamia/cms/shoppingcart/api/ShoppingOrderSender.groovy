package toosl.dynamia.cms.shoppingcart.api

interface ShoppingOrderSender {

	Response sendOrder(toosl.dynamia.cms.shoppingcart.dto.ShoppingOrderDTO dto, Map<String, String> params)

    List<Response> sendOrders(List<toosl.dynamia.cms.shoppingcart.dto.ShoppingOrderDTO> dtos, Map<String, String> params)
}
