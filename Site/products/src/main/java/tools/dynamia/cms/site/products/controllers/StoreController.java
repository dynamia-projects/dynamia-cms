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
package tools.dynamia.cms.site.products.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.CookieGenerator;

import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.actions.SiteActionManager;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.products.ProductSearchForm;
import tools.dynamia.cms.site.products.ProductShareForm;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.commons.StringUtils;

/**
 *
 * @author Mario Serrano Leones
 */
@Controller
@SessionAttributes("prd_searchForm")
@RequestMapping(value = "/store", method = RequestMethod.GET)
public class StoreController {

	public final static String RECENT_PRODUCTS_COOKIE_NAME = "CMSRPRD";

	@Autowired
	private ProductsService service;

	@Autowired
	private SiteService siteService;

	@RequestMapping(value = { "", "/", "/categories" }, method = RequestMethod.GET)
	public ModelAndView category(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("products/categories");

		SiteActionManager.performAction("showProductCategories", mv, request);

		return mv;

	}

	@RequestMapping(value = "/categories/{id}")
	public ModelAndView categoryid(@PathVariable Long id, HttpServletRequest request) {
		return category(id, null, request);
	}

	@RequestMapping(value = "/categories/{id}/{category}")
	public ModelAndView category(@PathVariable Long id, @PathVariable String category, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("products/products");

		SiteActionManager.performAction("loadProductCategory", mv, request, id);

		return mv;

	}

	@RequestMapping("/categories/{id}/{category}/{brand}")
	public ModelAndView categoryBrand(@PathVariable Long id, @PathVariable String category, @PathVariable String brand,
			HttpServletRequest request) {
		Site site = siteService.getSite(request);

		ProductSearchForm form = new ProductSearchForm();
		ProductBrand productBrand = service.getBrandByAlias(site, brand);
		if (productBrand != null) {
			form.setBrandId(productBrand.getId());
		}
		form.setCategoryId(id);

		ModelAndView mv = new ModelAndView("products/brand");
		SiteActionManager.performAction("showProductBrand", mv, request, form);
		return mv;
	}

	@RequestMapping("/search")
	public ModelAndView search(ProductSearchForm form, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasFieldErrors("maxPrice")) {
			form.setMaxPrice(null);
		}
		if (bindingResult.hasFieldErrors("minPrice")) {
			form.setMinPrice(null);
		}

		ModelAndView mv = new ModelAndView("products/productquery");

		SiteActionManager.performAction("searchProducts", mv, request, form);

		return mv;
	}

	@RequestMapping("/products/{id}")
	public ModelAndView product(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("products/product");
		SiteActionManager.performAction("showProduct", mv, request, id);
		setupCookie(request, response, id);

		return mv;
	}

	@RequestMapping("/products")
	public ModelAndView product(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("products/product");
		SiteActionManager.performAction("showProduct", mv, request);

		return mv;
	}

	@RequestMapping("/products/{id}/print")
	public ModelAndView printProduct(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("products/printproduct");
		SiteActionManager.performAction("showProduct", mv, request, id);
		setupCookie(request, response, id);

		return mv;
	}

	@Secured("ROLE_USER")
	@RequestMapping("/products/{id}/favorite")
	public ModelAndView productFavorite(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("products/product");
		if (UserHolder.get().isAuthenticated()) {
			System.out.println("USER LOGGED: " + UserHolder.get().getUserName());
		}
		mv = product(id, request, response);

		return mv;
	}

	@RequestMapping("/products/{id}/compare")
	public ModelAndView productCompare(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		SiteActionManager.performAction("addProductToCompareList", mv, request, response, id);
		return mv;
	}

	@RequestMapping("/products/{id}/compare/remove")
	public ModelAndView productNoCompare(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		SiteActionManager.performAction("removeProductFromCompareList", mv, request, response, id);
		return mv;
	}

	@RequestMapping("/brands")
	public ModelAndView brands(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("products/brands");
		SiteActionManager.performAction("showProductBrands", mv, request);
		return mv;
	}

	@RequestMapping("/brands/{name}")
	public ModelAndView brand(@PathVariable String name, HttpServletRequest request) {
		Site site = siteService.getSite(request);

		ProductSearchForm form = new ProductSearchForm();
		try {
			if (name != null && !name.equals("#null#")) {
				form.setBrandId(service.getBrandByAlias(site, name).getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ModelAndView mv = new ModelAndView("products/brand");
		SiteActionManager.performAction("showProductBrand", mv, request, form);
		return mv;
	}

	@RequestMapping("/compare/{ids}")
	public ModelAndView compare(@PathVariable String ids, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("products/compare");
		SiteActionManager.performAction("compareProducts", mv, request, redirectAttributes, ids.split(","));
		return mv;
	}

	@RequestMapping("/compare/clear")
	public ModelAndView compareClear(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("products/compare");
		SiteActionManager.performAction("compareClear", mv, request, redirectAttributes);
		return mv;
	}

	@RequestMapping("/compare/{ids}/print")
	public ModelAndView comparePrint(@PathVariable String ids, HttpServletRequest request) {
		Site site = siteService.getSite(request);

		ModelAndView mv = new ModelAndView("products/printcompare");
		SiteActionManager.performAction("compareProducts", mv, request, ids.split(","));
		return mv;
	}

	@RequestMapping("/stores")
	public ModelAndView stores(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("products/stores");
		SiteActionManager.performAction("showProductStores", mv, request);
		return mv;
	}

	@RequestMapping(value = "/products/{id}/share", method = RequestMethod.GET)
	public ModelAndView share(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("products/share");
		SiteActionManager.performAction("showProduct", mv, id);
		return mv;
	}

	@RequestMapping(value = "/share", method = RequestMethod.POST)
	public ModelAndView share(@Valid ProductShareForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		ModelAndView mv = new ModelAndView();
		mv.setView(new RedirectView("/store/products/" + form.getProductId(), true, true, false));
		if (!bindingResult.hasErrors()) {
			form.setSite(SiteContext.get().getCurrent());
			form.setProductURL(SiteContext.get().getSiteURL() + "/store/products/" + form.getProductId());
			SiteActionManager.performAction("shareProduct", mv, request, redirectAttributes, form);
		} else {
			CMSUtil.addErrorMessage("Todos los campos son requeridos para enviar a un amigo", redirectAttributes);
		}

		return mv;
	}

	private void setupCookie(HttpServletRequest request, HttpServletResponse response, Long id) {

		Site site = siteService.getSite(request);

		if (site == null) {
			return;
		}

		String idText = String.valueOf(id);
		String siteCookieName = RECENT_PRODUCTS_COOKIE_NAME + site.getKey();

		Cookie currentCookie = CMSUtil.getCookie(request, siteCookieName);

		List<String> ids = new ArrayList<>();
		if (currentCookie != null) {
			String value[] = currentCookie.getValue().split(",");
			ids.addAll(Arrays.asList(value));
		}

		if (ids.contains(idText)) {
			ids.remove(idText);
		}

		ids.add(0, idText);

		if (ids.size() > service.getSiteConfig(site).getProductsPerPage()) {
			ids.remove(ids.size() - 1);
		}

		CookieGenerator recentViews = new CookieGenerator();
		recentViews.setCookieName(siteCookieName);
		recentViews.setCookieMaxAge(30 * 24 * 60 * 60);
		recentViews.addCookie(response, StringUtils.arrayToCommaDelimitedString(ids.toArray()));
	}

}
