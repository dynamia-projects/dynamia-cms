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
package tools.dynamia.cms.products

import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.shoppingcart.ShoppingCartHolder
import tools.dynamia.cms.shoppingcart.domain.ShoppingCart
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.User
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
