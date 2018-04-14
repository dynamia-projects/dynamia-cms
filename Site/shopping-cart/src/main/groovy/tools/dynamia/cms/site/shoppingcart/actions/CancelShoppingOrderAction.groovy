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
package tools.dynamia.cms.site.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class CancelShoppingOrderAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "cancelShoppingOrder"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView
        mv.view = new RedirectView("/", true, true, false)

        ShoppingOrder order = ShoppingCartHolder.get().currentOrder
        if (order == null) {
			return
        }

		if (order.id != null) {
			order = crudService.find(ShoppingOrder.class, order.id)
            ShoppingCartHolder.get().currentOrder = order
        }
		order.sync()

        try {
			service.cancelOrder(order)
            CMSUtil.addSuccessMessage("Pedido No." + order.number + " cancelado exitosamente", mv)
        } catch (ValidationError e) {
			CMSUtil.addErrorMessage(e.message, mv)
        } catch (Exception e) {
			e.printStackTrace()
        }
	}

}
