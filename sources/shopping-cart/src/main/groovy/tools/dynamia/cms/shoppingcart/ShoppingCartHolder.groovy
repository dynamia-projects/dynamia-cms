/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.shoppingcart

import org.springframework.context.annotation.Scope
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.payment.PaymentHolder
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Component

/**
 *
 * @author Mario Serrano Leones
 */
@Component
@Scope("session")
class ShoppingCartHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7780236453596280623L
    private Map<String, tools.dynamia.cms.shoppingcart.domain.ShoppingCart> carts = new HashMap<String, tools.dynamia.cms.shoppingcart.domain.ShoppingCart>()
    private tools.dynamia.cms.shoppingcart.domain.ShoppingOrder currentOrder

    static ShoppingCartHolder get() {
		return Containers.get().findObject(ShoppingCartHolder.class)
    }

	/**
	 * Get a shopping cart with given name. If not exist it create a new one
	 * 
	 * @param name
	 * @return
	 */
	tools.dynamia.cms.shoppingcart.domain.ShoppingCart getCart(String name) {
		return getCart(name, "")
    }

    tools.dynamia.cms.shoppingcart.domain.ShoppingCart getCart(String name, String title) {
		tools.dynamia.cms.shoppingcart.domain.ShoppingCart cart = carts.get(name)
        if (cart == null) {
			cart = new tools.dynamia.cms.shoppingcart.domain.ShoppingCart(name)
            cart.title = title
            cart.site = SiteContext.get().current
            cart.user = UserHolder.get().current
            carts.put(name, cart)
        }
		return cart
    }

    void removeCart(String name) {
		carts.remove(name)
    }

    tools.dynamia.cms.shoppingcart.domain.ShoppingOrder getCurrentOrder() {
		return currentOrder
    }

    void setCurrentOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder currentOrder) {
		this.currentOrder = currentOrder
        if (currentOrder == null) {
            PaymentHolder.get().currentPaymentForm = null
            PaymentHolder.get().currentPaymentTransaction = null
        } else {
            PaymentHolder.get().currentPaymentTransaction = currentOrder.transaction
        }
	}

    void clearAll() {
		currentOrder = null
        carts.clear()
    }

}
