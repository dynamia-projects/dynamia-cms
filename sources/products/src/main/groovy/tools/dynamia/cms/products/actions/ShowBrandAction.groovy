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
package tools.dynamia.cms.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.actions.SiteActionManager
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
class ShowBrandAction implements SiteAction {

	@Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "showProductBrand"
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.modelAndView
        ProductSearchForm form = (ProductSearchForm) evt.data

        if (form.brandId != null) {
			ProductBrand brand = crudService.find(ProductBrand.class, form.brandId)

            mv.addObject("prd_brand", brand)
            form.order = tools.dynamia.cms.products.ProductSearchOrder.MINPRICE

            if (!evt.request.parameterMap.isEmpty()) {
                form.detail = evt.request.getParameter("q")
            }

			SiteActionManager.performAction("searchProducts", mv, evt.request, form)
            ProductCategory category = (ProductCategory) mv.model.get("prd_category")

            if (category != null) {
				mv.addObject("title", category.name)
                mv.addObject("prd_subcategories", service.getSubcategories(category, brand))
                mv.addObject("prd_category_details", service.getCategoryDetails(category))
            } else {
				mv.addObject("title", brand.name)
                mv.addObject("prd_categories", service.getCategories(brand))
            }

			if (category != null) {
				List<ProductBrand> categoryBrands = service.getBrands(category)
                mv.addObject("prd_categoryBrands", categoryBrands)
            }

		}
	}

}
