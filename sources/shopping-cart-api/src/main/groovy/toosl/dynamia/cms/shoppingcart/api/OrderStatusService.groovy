package toosl.dynamia.cms.shoppingcart.api

import toosl.dynamia.cms.shoppingcart.dto.OrderStatusDTO

interface OrderStatusService {

	List<OrderStatusDTO> getOrdersStatus(String customer)

}
