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
package tools.dynamia.cms.shoppingcart.services

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.domain.User

/**
 *
 * @author Mario Serrano Leones
 */
interface ShoppingCartService {

	tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem getItem(Site site, String code)

    abstract tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig getConfiguration(Site site)

    tools.dynamia.cms.shoppingcart.domain.ShoppingOrder createOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingCart shoppingCart, tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig config)

    abstract void cancelOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder order)

    abstract void saveOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder order)

    abstract void notifyOrderCompleted(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder order)

    abstract void shipOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder shoppingOrder)

    abstract void notifyOrderShipped(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder order)

    List<tools.dynamia.cms.shoppingcart.domain.ShoppingOrder> getOrders(User current)

    void sendOrder(tools.dynamia.cms.shoppingcart.domain.ShoppingOrder order)

    void sendAllOrders()

}
