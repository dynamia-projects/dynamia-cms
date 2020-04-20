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
