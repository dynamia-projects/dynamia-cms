/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart;

import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingOrder;
import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;

/**
 *
 * @author mario_2
 */
@Component
@Scope("session")
public class ShoppingCartHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7780236453596280623L;
	private Map<String, ShoppingCart> carts = new HashMap<String, ShoppingCart>();
	private ShoppingOrder currentOrder;

	public static ShoppingCartHolder get() {
		return Containers.get().findObject(ShoppingCartHolder.class);
	}

	/**
	 * Get a shopping cart with given name. If not exist it create a new one
	 * 
	 * @param name
	 * @return
	 */
	public ShoppingCart getCart(String name) {
		ShoppingCart cart = carts.get(name);
		if (cart == null) {
			cart = new ShoppingCart(name);
			cart.setSite(SiteContext.get().getCurrent());
			if (UserHolder.get().isAuthenticated()) {
				cart.setUser(UserHolder.get().getCurrent());
			}
			carts.put(name, cart);
		}
		return cart;
	}

	public void removeCart(String name) {
		carts.remove(name);
	}

	public ShoppingOrder getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(ShoppingOrder currentOrder) {
		this.currentOrder = currentOrder;
	}

}
