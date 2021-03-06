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

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.integration.Containers

class ShoppingCartUtils {

    static ShoppingCart getShoppingCart(ModelAndView mv) {
        String cartName = (String) mv.model.get("cartName")
        ShoppingCart cart = ShoppingCartHolder.get().getCart(cartName)
        mv.addObject("cart", cart)
        return cart
    }

    static void loadConfig(Site site, ModelAndView mv) {
        ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class)

        ShoppingSiteConfig config = service.getConfiguration(site)
        SiteAware shoppingCart = getShoppingCart(mv)
        mv.addObject("shoppingConfig", config)
        boolean paymentEnabled = false
        if (config != null) {
            paymentEnabled = config.paymentEnabled || UserHolder.get().admin
        }
        mv.addObject("paymentEnabled", paymentEnabled)

    }


}
