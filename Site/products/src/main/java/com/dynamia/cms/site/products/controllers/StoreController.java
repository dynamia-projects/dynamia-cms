/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.controllers;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.commons.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

/**
 *
 * @author mario
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

    @RequestMapping("/")
    public String home() {
        return null;
    }

    @RequestMapping(value = "/categories")
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
    public ModelAndView categoryBrand(@PathVariable Long id, @PathVariable String category, @PathVariable String brand, HttpServletRequest request) {
        Site site = siteService.getSite(request);

        ProductSearchForm form = new ProductSearchForm();
        form.setBrandId(service.getBrandByAlias(site, brand).getId());
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

    @Secured("ROLE_USER")
    @RequestMapping("/products/{id}/favorite")
    public ModelAndView productFavorite(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("products/product");
        if (UserHolder.get().isAuthenticated()) {
            System.out.println("USER LOGGED: " + UserHolder.get().getUserName());
        }
        mv = product(id, request, response);

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
        form.setBrandId(service.getBrandByAlias(site, name).getId());

        ModelAndView mv = new ModelAndView("products/brand");
        SiteActionManager.performAction("showProductBrand", mv, request, form);
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
