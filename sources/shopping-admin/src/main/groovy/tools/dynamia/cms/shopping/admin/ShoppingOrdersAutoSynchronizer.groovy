package tools.dynamia.cms.shopping.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService

@Service
class ShoppingOrdersAutoSynchronizer {

	@Autowired
	private ShoppingCartService service

	@Scheduled(fixedRate = 300000L)
	void sync() {
		service.sendAllOrders()
	}
}
