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
package tools.dynamia.cms.products.ext

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.pages.PageContext
import tools.dynamia.cms.pages.api.PageTypeExtension
import tools.dynamia.cms.pages.domain.PageParameter
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSExtension
class ProductsPageType implements PageTypeExtension {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private CrudService crudService

    @Override
    String getId() {
        return "products"
    }

    @Override
    String getName() {
        return "Products List Page"
    }

    @Override
    String getDescription() {
        return "A product list page, you cant specified a category id using page parameters"
    }

    @Override
    String getDescriptorId() {
        return "ProductGridConfig"
    }

    @Override
    void setupPage(PageContext context) {
        ModelAndView mv = context.modelAndView
        mv.addObject("subtitle", context.page.subtitle)

        PageParameter categoryParam = context.getParameter("category")

        if (categoryParam != null) {
            Long id = new Long(categoryParam.value)
            SiteActionManager.performAction("loadProductCategory", mv, context.request, id)
            mv.viewName = "products/products"
        } else {
            tools.dynamia.cms.products.ProductsUtil.setupProductsVar(service.getFeaturedProducts(context.site), context.modelAndView)
            mv.viewName = "products/main"
            SiteActionManager.performAction("showMainProductPage", mv, context.request)

        }

    }

}
