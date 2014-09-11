package com.dynamia.cms.site.shoppingcart;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;

public class ShoppingCartUtils {

	public static ShoppingCart getShoppingCart(ModelAndView mv) {
		String cartName = (String) mv.getModel().get("cartName");
		ShoppingCart cart = ShoppingCartHolder.get().getCart(cartName);
		mv.addObject("cart", cart);
		return cart;
	}

}
