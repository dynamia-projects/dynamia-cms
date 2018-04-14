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
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.UserContactInfo
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ViewShoppingOrderAction implements SiteAction {

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "viewShoppingOrder";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/order");

		mv.addObject("title", "Pedido");

		Long id = (Long) evt.getData();

		ShoppingOrder order = null;

		if (id != null) {
			order = crudService.find(ShoppingOrder.class, id);
		}
		
		if (order == null || !order.getShoppingCart().getUser().equals(UserHolder.get().getCurrent())) {
			SiteActionManager.performAction("showMyShoppingOrders", mv, evt.getRequest(), evt.getRedirectAttributes());
			CMSUtil.addErrorMessage("No se encontro pedido", mv);
		} else {
			mv.addObject("shoppingOrder", order);
		}

	}

	private void validate(ShoppingOrder order, ShoppingSiteConfig config) {
		if (!order.isPayLater() || !config.isAllowEmptyPayment()) {
			throw new ValidationError("El sitio web no permite este tipo de pedidos");
		}

	}

	private UserContactInfo loadContactInfo(String string, ActionEvent evt) {
		UserContactInfo userContactInfo = null;

		try {
			Long id = Long.parseLong(evt.getRequest().getParameter(string));
			userContactInfo = crudService.find(UserContactInfo.class, id);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return userContactInfo;
	}

}
