package toosl.dynamia.cms.site.shoppingcart.api;

import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderDTO;

public interface ShoppingOrderSender {

	
	public String sendOrder(ShoppingOrderDTO dto);
}
