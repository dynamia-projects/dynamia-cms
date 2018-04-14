package toosl.dynamia.cms.site.shoppingcart.api

import toosl.dynamia.cms.site.shoppingcart.dto.OrderStatusDTO

interface OrderStatusService {

	List<OrderStatusDTO> getOrdersStatus(String customer)

}
