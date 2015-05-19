package com.dynamia.cms.site.shoppingcart;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import com.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.integration.Containers;

public class ShoppingCartUtils {

	public static ShoppingCart getShoppingCart(ModelAndView mv) {
		String cartName = (String) mv.getModel().get("cartName");
		ShoppingCart cart = ShoppingCartHolder.get().getCart(cartName);
		mv.addObject("cart", cart);
		return cart;
	}

	public static void loadConfig(Site site, ModelAndView mv) {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class);

		ShoppingSiteConfig config = service.getConfiguration(site);
		ShoppingCart shoppingCart = getShoppingCart(mv);
		mv.addObject("shoppingConfig", config);
		boolean paymentEnabled = false;
		if (config != null) {
			paymentEnabled = config.isPaymentEnabled() || UserHolder.get().isSuperadmin();
		}
		mv.addObject("paymentEnabled", paymentEnabled);

	}

}
