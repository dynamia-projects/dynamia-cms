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
package tools.dynamia.cms.site.products.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.actions.SiteActionManager
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.products.ProductSearchForm
import tools.dynamia.cms.site.products.ProductSearchOrder
import tools.dynamia.cms.site.products.domain.ProductBrand
import tools.dynamia.cms.site.products.domain.ProductCategory
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.domain.services.CrudService

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowBrandAction implements SiteAction {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "showProductBrand";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		ProductSearchForm form = (ProductSearchForm) evt.getData();

		if (form.getBrandId() != null) {
			ProductBrand brand = crudService.find(ProductBrand.class, form.getBrandId());

			mv.addObject("prd_brand", brand);
			form.setOrder(ProductSearchOrder.MINPRICE);

			if (!evt.getRequest().getParameterMap().isEmpty()) {
				form.setDetail(evt.getRequest().getParameter("q"));
			}

			SiteActionManager.performAction("searchProducts", mv, evt.getRequest(), form);
			ProductCategory category = (ProductCategory) mv.getModel().get("prd_category");

			if (category != null) {
				mv.addObject("title", category.getName());
				mv.addObject("prd_subcategories", service.getSubcategories(category, brand));
				mv.addObject("prd_category_details", service.getCategoryDetails(category));
			} else {
				mv.addObject("title", brand.getName());
				mv.addObject("prd_categories", service.getCategories(brand));
			}

			if (category != null) {
				List<ProductBrand> categoryBrands = service.getBrands(category);
				mv.addObject("prd_categoryBrands", categoryBrands);
			}

		}
	}

}
