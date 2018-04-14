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
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.products.controllers.StoreController
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.commons.StringUtils
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.services.CrudService

import javax.servlet.http.Cookie

/**
 *
 * @author Mario Serrano Leones
 */
@CMSAction
public class ShowProductsMainPageAction implements SiteAction {

	private LoggingService logger = new SLF4JLoggingService(ShowProductsMainPageAction.class);

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "showMainProductPage";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.getModelAndView();

		List<Product> mostViewed = service.getMostViewedProducts(evt.getSite());
		List<Product> priceVariations = service.getPriceVariationsProducts(evt.getSite());

		mv.addObject("prd_mostViewedProducts", mostViewed);
		mv.addObject("prd_priceVariations", priceVariations);

		if (UserHolder.get().isAuthenticated()) {
			loadRecentProductsFromUser(evt, mv);
		} else {
			// loadRecentProductsFromCookies(evt, mv);
		}

		applyParams(mv);

	}

	private void applyParams(ModelAndView mv) {
		Map<String, Object> pageParams = (Map<String, Object>) mv.getModel().get("pageParams");

	}

	private void loadRecentProductsFromUser(ActionEvent evt, ModelAndView mv) {
		try {

			List<Product> recentViewed = service.getRecentProducts(UserHolder.get().getCurrent());
			Product firstProduct = recentViewed.get(0);
			List<Product> relatedProducts = service.getRelatedCategoryProducts(firstProduct);
			mv.addObject("prd_recentViewedProducts", recentViewed);
			mv.addObject("prd_relatedProducts", relatedProducts);
		} catch (Exception e) {
			System.out.println("ERROR loadRecentProductsFromUser " + e.getMessage());
		}

	}

	private void loadRecentProductsFromCookies(ActionEvent evt, ModelAndView mv) throws NumberFormatException {
		try {
			Cookie cookie = CMSUtil.getCookie(evt.getRequest(),
					StoreController.RECENT_PRODUCTS_COOKIE_NAME + evt.getSite().getKey());
			if (cookie != null) {
				List<Long> ids = new ArrayList<>();
				String values[] = StringUtils.commaDelimitedListToStringArray(cookie.getValue());
				for (String idText : values) {
					ids.add(new Long(idText));
				}
				if (!ids.isEmpty()) {

					List<Product> recentViewed = service.getProductsById(ids);
					recentViewed = sortByIdList(recentViewed, ids);

					Product firstProduct = recentViewed.get(0);
					List<Product> relatedProducts = service.getRelatedCategoryProducts(firstProduct);
					mv.addObject("prd_recentViewedProducts", recentViewed);
					mv.addObject("prd_relatedProducts", relatedProducts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error processing RECENT PRODUCTS COOKIE", e);
		}
	}

	private List<Product> sortByIdList(List<Product> list, List<Long> ids) {
		List<Product> resultList = new ArrayList<>();
		for (Long id : ids) {
			for (Product product : list) {
				if (product.getId().equals(id)) {
					resultList.add(product);
					break;
				}
			}
		}
		return resultList;

	}

}
