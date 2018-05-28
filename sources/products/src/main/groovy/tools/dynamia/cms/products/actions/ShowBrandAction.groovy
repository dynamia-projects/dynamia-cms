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
