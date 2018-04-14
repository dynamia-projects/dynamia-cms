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

import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.shoppingcart.ShoppingCartUtils

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class PrintShoppingCartAction implements SiteAction {

	@Override
	public String getName() {
		return "printShoppingCart";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ShoppingCartUtils.getShoppingCart(evt.getModelAndView());
		evt.getModelAndView().setViewName("shoppingcart/print");

	}

}
