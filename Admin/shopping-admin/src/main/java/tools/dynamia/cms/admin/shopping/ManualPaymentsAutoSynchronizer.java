package tools.dynamia.cms.admin.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;

@Service
public class ManualPaymentsAutoSynchronizer {

	@Autowired
	private ShoppingCartService service;

	@Scheduled(fixedRate = 8 * 60 * 1000)
	public void sync() {
		service.sendAllPayments();
	}
}