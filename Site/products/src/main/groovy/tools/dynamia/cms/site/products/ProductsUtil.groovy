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
package tools.dynamia.cms.site.products

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.integration.Containers

/**
 *
 * @author Mario Serrano Leones
 */
class ProductsUtil {

	static ProductsUtil get() {
		return new ProductsUtil()
    }

    static void setupDefaultVars(Site site, ModelAndView mv) {
		ProductsService service = Containers.get().findObject(ProductsService.class)

        mv.addObject("prdUtil", ProductsUtil.get())
        mv.addObject("prd_categories", service.getCategories(site))
        mv.addObject("prd_brands", service.getBrands(site))
        mv.addObject("prd_config", service.getSiteConfig(site))
        if (mv.model.get("prd_searchForm") == null) {
			mv.addObject("prd_searchForm", new ProductSearchForm())
        }
		if (mv.model.get("prd_product") != null) {
			addShareForm(site, mv)
        }

		if (mv.model.get("cart") != null) {
			try {
				Object cart = mv.model.get("cart")
                if(cart instanceof String) {
					cart = ShoppingCartHolder.get().getCart((String) cart)
                }
				
				ShoppingCart shoppingCart = (ShoppingCart) cart
                if (shoppingCart != null && shoppingCart.name != null) {
					switch (shoppingCart.name) {
					case "quote":
						mv.addObject("title", "Cotizacion")
                        mv.addObject("icon", "icon-external-link")
                        break
                        case "shop":
						mv.addObject("title", "Carrito de Compra")
                            mv.addObject("icon", "icon-shopping-cart")
                            break
                    }
				}
			} catch (Exception e) {
				e.printStackTrace()
            }
		}
	}

	private static void addShareForm(Site site, ModelAndView mv) {
		Product product = (Product) mv.model.get("prd_product")
        ProductShareForm form = new ProductShareForm(site)
        form.productId = product.id

        if (UserHolder.get().authenticated) {
            form.yourName = UserHolder.get().fullName
        }

		mv.addObject("prd_shareForm", form)

    }

    static void setupProductsVar(List<Product> products, ModelAndView mv) {
		mv.addObject("prd_products", products)
    }

    static void setupProductsVar(List<Product> products, Map<String, Object> map) {
		map.put("prd_products", products)
    }

    static void setupProductVar(Product product, ModelAndView mv) {
		mv.addObject("product", product)
    }

    BigDecimal getUserPrice(Product product, ProductsSiteConfig cfg) {
		User user = UserHolder.get().current
        User customer = UserHolder.get().customer

        String group = null

        if (customer != null) {
			group = customer.groupName
        } else if (user != null) {
			group = user.groupName
        }

		if (cfg == null || group == null || group.empty) {
			return product.realPrice
        } else {

			if (cfg.priceUserGroup != null && cfg.priceUserGroup.contains(group)) {
				return product.price
            } else if (cfg.price2UserGroup != null && cfg.price2UserGroup.contains(group)) {
				return product.price2
            } else if (cfg.costUserGroup != null && cfg.costUserGroup.contains(group)) {
				return product.price
            } else {
				return product.realPrice
            }
		}

	}

}
