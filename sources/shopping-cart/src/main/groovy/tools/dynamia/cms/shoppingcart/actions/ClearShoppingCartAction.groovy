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
package tools.dynamia.cms.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ClearShoppingCartAction implements SiteAction {

	@Autowired
	private tools.dynamia.cms.shoppingcart.services.ShoppingCartService service

    @Override
    String getName() {
		return "clearShoppingCart"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView

        tools.dynamia.cms.shoppingcart.domain.ShoppingCart shoppingCart = tools.dynamia.cms.shoppingcart.ShoppingCartUtils.getShoppingCart(mv)
        if (shoppingCart != null) {
			shoppingCart.items.clear()
            shoppingCart.compute()

            if (shoppingCart.name.equals("shop")) {
                tools.dynamia.cms.shoppingcart.ShoppingCartHolder.get().currentOrder = null
            }

			CMSUtil.addSuccessMessage("Carrito limpiado exitosamente", evt.redirectAttributes)
        }

	}

}