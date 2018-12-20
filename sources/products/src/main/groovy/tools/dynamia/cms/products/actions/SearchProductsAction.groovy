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
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class SearchProductsAction implements SiteAction {

    @Autowired
    private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
    private CrudService crudService

    @Override
    String getName() {
        return "searchProducts"
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.modelAndView
        Site site = evt.site

        List<Product> products = null
        if (evt.data instanceof String) {
            String query = (String) evt.data
            products = service.find(site, query)
        } else if (evt.data instanceof ProductSearchForm) {
            ProductSearchForm form = (ProductSearchForm) evt.data
            products = service.filterProducts(site, form)
            mv.addObject("prd_searchForm", form)
            if (form.categoryId != null) {
                ProductCategory category = crudService.find(ProductCategory.class, form.categoryId)
                List<ProductCategory> subcategories = service.getSubcategories(category)

                mv.addObject("prd_category", category)
                mv.addObject("prd_parentCategory", category.parent)
                mv.addObject("prd_subcategories", subcategories)
            }
        }

        if (products == null) {
            products = service.getFeaturedProducts(site)
            mv.addObject("title", "Ingrese los campos de busqueda")
        } else if (!products.empty) {
            mv.addObject("title", products.size() + " productos encontrados")
        } else {
            mv.addObject("title", " No se encontraron productos para la busqueda avanzada")
        }

        products = CMSUtil.setupPagination(products, evt.request, mv)
        tools.dynamia.cms.products.ProductsUtil.setupProductsVar(products, mv)

    }

}
