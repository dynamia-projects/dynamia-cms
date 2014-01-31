/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.controllers;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.services.ProductsService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
@SessionAttributes("prd_searchForm")
@RequestMapping(value = "/store", method = RequestMethod.GET)
public class StoreController {

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

        SiteActionManager.performAction("showCategories", mv, request);

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
        SiteActionManager.performAction("showBrand", mv, request, form);
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
    public ModelAndView product(@PathVariable Long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/product");
        SiteActionManager.performAction("showProduct", mv, request, id);
        return mv;

    }

    @RequestMapping("/brands")
    public ModelAndView brands(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/brands");
        SiteActionManager.performAction("showBrands", mv, request);
        return mv;
    }

    @RequestMapping("/brands/{name}")
    public ModelAndView brand(@PathVariable String name, HttpServletRequest request) {
        Site site = siteService.getSite(request);

        ProductSearchForm form = new ProductSearchForm();
        form.setBrandId(service.getBrandByAlias(site, name).getId());

        ModelAndView mv = new ModelAndView("products/brand");
        SiteActionManager.performAction("showBrand", mv, request, form);
        return mv;
    }

}
