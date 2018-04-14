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
package tools.dynamia.cms.site.shoppingcart.services

import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.users.domain.User

/**
 *
 * @author Mario Serrano Leones
 */
interface ShoppingCartService {

	ShoppingCartItem getItem(Site site, String code)

    abstract ShoppingSiteConfig getConfiguration(Site site)

    ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config)

    abstract void cancelOrder(ShoppingOrder order)

    abstract void saveOrder(ShoppingOrder order)

    abstract void notifyOrderCompleted(ShoppingOrder order)

    abstract void shipOrder(ShoppingOrder shoppingOrder)

    abstract void notifyOrderShipped(ShoppingOrder order)

    List<ShoppingOrder> getOrders(User current)

    void sendOrder(ShoppingOrder order)

    void sendAllOrders()

}
