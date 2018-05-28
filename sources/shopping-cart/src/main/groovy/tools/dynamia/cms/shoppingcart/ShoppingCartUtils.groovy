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

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.integration.Containers

class ShoppingCartUtils {

	static tools.dynamia.cms.shoppingcart.domain.ShoppingCart getShoppingCart(ModelAndView mv) {
		String cartName = (String) mv.model.get("cartName")
        tools.dynamia.cms.shoppingcart.domain.ShoppingCart cart = ShoppingCartHolder.get().getCart(cartName)
        mv.addObject("cart", cart)
        return cart
    }

    static void loadConfig(Site site, ModelAndView mv) {
		ShoppingCartService service = Containers.get().findObject(ShoppingCartService.class)

        tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig config = service.getConfiguration(site)
        SiteAware shoppingCart = getShoppingCart(mv)
        mv.addObject("shoppingConfig", config)
        boolean paymentEnabled = false
        if (config != null) {
			paymentEnabled = config.paymentEnabled || UserHolder.get().admin
        }
		mv.addObject("paymentEnabled", paymentEnabled)

    }
	
	

}
