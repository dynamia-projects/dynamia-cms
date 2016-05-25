/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.shoppingcart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;

import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.users.UserHolder;

import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Component;

/**
 *
 * @author Mario Serrano Leones
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
		return getCart(name, "");
	}

	public ShoppingCart getCart(String name, String title) {
		ShoppingCart cart = carts.get(name);
		if (cart == null) {
			cart = new ShoppingCart(name);
			cart.setTitle(title);
			cart.setSite(SiteContext.get().getCurrent());
			cart.setUser(UserHolder.get().getCurrent());
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
