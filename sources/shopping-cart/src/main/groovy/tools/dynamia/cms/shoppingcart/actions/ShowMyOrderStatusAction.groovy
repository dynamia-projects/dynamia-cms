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
package tools.dynamia.cms.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.BigDecimalUtils
import tools.dynamia.web.util.HttpRemotingServiceClient
import toosl.dynamia.cms.shoppingcart.api.OrderStatusService
import toosl.dynamia.cms.shoppingcart.dto.OrderStatusDTO

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ShowMyOrderStatusAction implements SiteAction {

    @Autowired
    private ShoppingCartService service

    @Autowired
    private UserService userService

    @Override
    String getName() {
        return "showMyOrdersStatus"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        mv.viewName = "shoppingcart/myorderstatus"

        mv.addObject("title", "Estado de Cuenta")

        ShoppingSiteConfig cfg = service.getConfiguration(evt.site)
        String customer = (String) evt.data

        if (customer == null) {
            if (UserHolder.get().customer != null) {
                customer = UserHolder.get().customer.externalRef
            } else if (UserHolder.get().current.profile == UserProfile.USER) {
                customer = UserHolder.get().current.externalRef
            }
        }

        List<OrderStatusDTO> orders = new ArrayList<>()
        User userCustomer = userService.getByExternalRef(evt.site, customer)
        mv.addObject("customer", userCustomer)
        try {
            if (cfg.orderStatusURL != null && !cfg.orderStatusURL.empty && customer != null) {
                OrderStatusService service = HttpRemotingServiceClient.build(OrderStatusService.class)
                        .setServiceURL(cfg.orderStatusURL)
                        .getProxy()


                orders = service.getOrdersStatus(customer)


            } else {
                CMSUtil.addErrorMessage("No se ha configurado estado de cuenta", mv)

            }
        } catch (Exception e) {
            e.printStackTrace()
            CMSUtil.addWarningMessage("No se puede consultar estado de cuenta en este momento, intente mas tarde", mv)
        }

        if (customer == null) {
            CMSUtil.addErrorMessage("No se ha seleccionado cliente", mv)
        }

        mv.addObject("orders", orders)
        mv.addObject("sumTotal", BigDecimalUtils.sum("total", orders))
        mv.addObject("sumPaid", BigDecimalUtils.sum("paid", orders))
        mv.addObject("sumBalance", BigDecimalUtils.sum("balance", orders))

    }

}
