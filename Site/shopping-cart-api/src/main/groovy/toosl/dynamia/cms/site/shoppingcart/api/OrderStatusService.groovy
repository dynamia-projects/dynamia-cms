package toosl.dynamia.cms.site.shoppingcart.api;

import java.util.List;

import toosl.dynamia.cms.site.shoppingcart.dto.OrderStatusDTO;

public interface OrderStatusService {

	public List<OrderStatusDTO> getOrdersStatus(String customer);

}
