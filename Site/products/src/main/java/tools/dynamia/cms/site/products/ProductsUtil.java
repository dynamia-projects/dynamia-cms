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
package tools.dynamia.cms.site.products;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import tools.dynamia.cms.site.users.UserHolder;

import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
public class ProductsUtil {

	public static void setupDefaultVars(Site site, ModelAndView mv) {
		ProductsService service = Containers.get().findObject(ProductsService.class);

		mv.addObject("prd_categories", service.getCategories(site));
		mv.addObject("prd_brands", service.getBrands(site));
		mv.addObject("prd_config", service.getSiteConfig(site));
		if (mv.getModel().get("prd_searchForm") == null) {
			mv.addObject("prd_searchForm", new ProductSearchForm());
		}
		if (mv.getModel().get("prd_product") != null) {
			addShareForm(site, mv);
		}

		if (mv.getModel().get("cart") != null) {
			try {
				ShoppingCart shoppingCart = (ShoppingCart) mv.getModel().get("cart");
				if (shoppingCart != null && shoppingCart.getName()!=null) {
					switch (shoppingCart.getName()) {
					case "quote":
						mv.addObject("title", "Cotizacion");
						mv.addObject("icon", "icon-external-link");
						break;
					case "shop":
						mv.addObject("title", "Carrito de Compra");
						mv.addObject("icon", "icon-shopping-cart");
						break;
					}
				}
			} catch (Exception e) {
				System.err.println("Error Loading shopping CART");
				e.printStackTrace();
			}
		}
	}

	private static void addShareForm(Site site, ModelAndView mv) {
		Product product = (Product) mv.getModel().get("prd_product");
		ProductShareForm form = new ProductShareForm(site);
		form.setProductId(product.getId());

		if (UserHolder.get().isAuthenticated()) {
			form.setYourName(UserHolder.get().getFullName());
		}

		mv.addObject("prd_shareForm", form);

	}

	public static void setupProductsVar(List<Product> products, ModelAndView mv) {
		mv.addObject("prd_products", products);
	}

	public static void setupProductsVar(List<Product> products, Map<String, Object> map) {
		map.put("prd_products", products);
	}

	public static void setupProductVar(Product product, ModelAndView mv) {
		mv.addObject("product", product);
	}

}
