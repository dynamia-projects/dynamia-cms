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
