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
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.users.UserHolder
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
class ShowProductsMainPageAction implements SiteAction {

	private LoggingService logger = new SLF4JLoggingService(ShowProductsMainPageAction.class)

    @Autowired
	private tools.dynamia.cms.products.services.ProductsService service

    @Autowired
	private CrudService crudService

    @Override
    String getName() {
		return "showMainProductPage"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.modelAndView

        List<Product> mostViewed = service.getMostViewedProducts(evt.site)
        List<Product> priceVariations = service.getPriceVariationsProducts(evt.site)

        mv.addObject("prd_mostViewedProducts", mostViewed)
        mv.addObject("prd_priceVariations", priceVariations)

        if (UserHolder.get().authenticated) {
			loadRecentProductsFromUser(evt, mv)
        } else {
			// loadRecentProductsFromCookies(evt, mv);
		}

		applyParams(mv)

    }

	private void applyParams(ModelAndView mv) {
		Map<String, Object> pageParams = (Map<String, Object>) mv.model.get("pageParams")

    }

	private void loadRecentProductsFromUser(ActionEvent evt, ModelAndView mv) {
		try {

			List<Product> recentViewed = service.getRecentProducts(UserHolder.get().current)
            Product firstProduct = recentViewed.get(0)
            List<Product> relatedProducts = service.getRelatedCategoryProducts(firstProduct)
            mv.addObject("prd_recentViewedProducts", recentViewed)
            mv.addObject("prd_relatedProducts", relatedProducts)
        } catch (Exception e) {
			System.out.println("ERROR loadRecentProductsFromUser " + e.message)
        }

	}

	private void loadRecentProductsFromCookies(ActionEvent evt, ModelAndView mv) throws NumberFormatException {
		try {
			Cookie cookie = CMSUtil.getCookie(evt.request,
					tools.dynamia.cms.products.controllers.StoreController.RECENT_PRODUCTS_COOKIE_NAME + evt.site.key)
            if (cookie != null) {
				List<Long> ids = new ArrayList<>()
                String[] values = StringUtils.commaDelimitedListToStringArray(cookie.value)
                for (String idText : values) {
					ids.add(new Long(idText))
                }
				if (!ids.isEmpty()) {

					List<Product> recentViewed = service.getProductsById(ids)
                    recentViewed = sortByIdList(recentViewed, ids)

                    Product firstProduct = recentViewed.get(0)
                    List<Product> relatedProducts = service.getRelatedCategoryProducts(firstProduct)
                    mv.addObject("prd_recentViewedProducts", recentViewed)
                    mv.addObject("prd_relatedProducts", relatedProducts)
                }
			}
		} catch (Exception e) {
			e.printStackTrace()
            logger.error("Error processing RECENT PRODUCTS COOKIE", e)
        }
	}

	private List<Product> sortByIdList(List<Product> list, List<Long> ids) {
		List<Product> resultList = new ArrayList<>()
        for (Long id : ids) {
			for (Product product : list) {
				if (product.id == id) {
					resultList.add(product)
                    break
                }
			}
		}
		return resultList

    }

}
