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
