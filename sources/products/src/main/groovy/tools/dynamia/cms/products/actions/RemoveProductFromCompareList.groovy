/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class RemoveProductFromCompareList implements SiteAction {

    @Autowired
    private CrudService crudService
    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Override
    String getName() {
        return "removeProductFromCompareList"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        Long id = (Long) evt.data

        Product product = crudService.find(Product.class, id)
        tools.dynamia.cms.products.ProductCompareList list = Containers.get().findObject(tools.dynamia.cms.products.ProductCompareList.class)
        list.remove(product)

        ModelAndView mv = evt.modelAndView
        String action = "/store/compare/" + list.toString()
        tools.dynamia.cms.products.ProductsUtil.setupDefaultVars(evt.site, mv)
        if (list.products.size() == 1) {
            CMSUtil.addSuccessMessage("Agregue otros productos de la misma categoria o similares para comparar", evt.redirectAttributes)
        } else if (list.products.empty) {
            action = "/"
        }
        mv.view = new RedirectView(action, true, true, false)

    }

}
