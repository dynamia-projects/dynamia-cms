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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.actions.ActionEvent;
import tools.dynamia.cms.site.core.actions.SiteAction;
import tools.dynamia.cms.site.core.api.CMSAction;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.services.UserService;
import tools.dynamia.commons.BigDecimalUtils;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.web.util.HttpRemotingServiceClient;
import toosl.dynamia.cms.site.shoppingcart.api.OrderStatusService;
import toosl.dynamia.cms.site.shoppingcart.dto.OrderStatusDTO;

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowMyOrderStatusAction implements SiteAction {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private UserService userService;

	@Override
	public String getName() {
		return "showMyOrdersStatus";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		mv.setViewName("shoppingcart/myorderstatus");

		mv.addObject("title", "Estado de Cuenta");

		ShoppingSiteConfig cfg = service.getConfiguration(evt.getSite());
		String customer = (String) evt.getData();

		if (customer == null) {
			if (UserHolder.get().getCustomer() != null) {
				customer = UserHolder.get().getCustomer().getExternalRef();
			} else if (UserHolder.get().getCurrent().getProfile() == UserProfile.USER) {
				customer = UserHolder.get().getCurrent().getExternalRef();
			}
		}

		List<OrderStatusDTO> orders = new ArrayList<>();

		try {
			if (cfg.getOrderStatusURL() != null && !cfg.getOrderStatusURL().isEmpty() && customer != null) {
				OrderStatusService service = HttpRemotingServiceClient.build(OrderStatusService.class)
						.setServiceURL(cfg.getOrderStatusURL()).getProxy();

				orders = service.getOrdersStatus(customer);

				User userCustomer = userService.getByExternalRef(evt.getSite(), customer);
				mv.addObject("customer", userCustomer);
				

			} else {
				CMSUtil.addErrorMessage("No se ha configurado estado de cuenta", mv);

			}
		} catch (Exception e) {
			e.printStackTrace();
			CMSUtil.addWarningMessage("No se puede consultar estado de cuenta en este momento, intente mas tarde", mv);
		}

		if (customer == null) {
			CMSUtil.addErrorMessage("No se ha seleccionado cliente", mv);
		}

		mv.addObject("orders", orders);
		mv.addObject("sumTotal",BigDecimalUtils.sum("total", orders));
		mv.addObject("sumPaid",BigDecimalUtils.sum("paid", orders));
		mv.addObject("sumBalance",BigDecimalUtils.sum("balance", orders));

	}

}
