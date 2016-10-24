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
package tools.dynamia.cms.site.shoppingcart.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.cms.site.users.UserHolder;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowMyShoppingOrdersAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Override
	public String getName() {
		return "showMyShoppingOrders";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/myorders");

		mv.addObject("title", "Mis Pedidos");

		List<ShoppingOrder> orders = service.getOrders(UserHolder.get().getCurrent());

		mv.addObject("orders", orders);

	}

}
