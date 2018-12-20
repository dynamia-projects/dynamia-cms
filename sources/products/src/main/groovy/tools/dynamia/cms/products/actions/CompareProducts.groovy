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
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class CompareProducts implements SiteAction {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service
    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "compareProducts"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        String[] idsString = (String[]) evt.data
        List<Long> ids = new ArrayList<>()
        for (String id : idsString) {
            ids.add(new Long(id))
        }

        List<Product> products = service.getProductsById(ids)
        tools.dynamia.cms.products.ProductCompareGrid grid = new tools.dynamia.cms.products.ProductCompareGrid(service, products)
        grid.loadData()

        ModelAndView mv = evt.modelAndView
        mv.addObject("comparegrid", grid)
        mv.addObject("title", buildTitle(products, evt.site))
        tools.dynamia.cms.products.ProductsUtil.setupDefaultVars(evt.site, mv)
    }

    private String buildTitle(List<Product> products, Site site) {
        CMSUtil utils = new CMSUtil(site)
        List<String> names = new ArrayList<>()
        for (Product product : products) {
            names.add(utils.cropText(product.name, 30).replace(".", "").trim().toUpperCase())
        }
        return StringUtils.arrayToDelimitedString(names.toArray(), " vs ")
    }

}
