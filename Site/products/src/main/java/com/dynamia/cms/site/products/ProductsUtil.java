/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import com.dynamia.cms.site.users.UserHolder;

import tools.dynamia.commons.collect.PagedList;
import tools.dynamia.commons.collect.PagedListDataSource;
import tools.dynamia.integration.Containers;

/**
 *
 * @author mario
 */
public class ProductsUtil {

	private static final String DATASOURCE = "productsDatasource";

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

	public static List<Product> setupPagination(List<Product> products, HttpServletRequest request, ModelAndView mv) {
		PagedListDataSource datasource = (PagedListDataSource) request.getSession().getAttribute(DATASOURCE);

		if (products instanceof PagedList) {
			datasource = ((PagedList) products).getDataSource();
			request.getSession().setAttribute(DATASOURCE, datasource);
		}

		if (datasource != null) {
			mv.addObject(DATASOURCE, datasource);

			if (request.getParameter("page") != null) {
				try {
					int page = Integer.parseInt(request.getParameter("page"));
					datasource.setActivePage(page);
				} catch (NumberFormatException numberFormatException) {
					// not a number, ignore it
				}
			}

			products = datasource.getPageData();
		}
		return products;
	}

}
