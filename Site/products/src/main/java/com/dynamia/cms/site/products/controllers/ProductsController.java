/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.controllers;

import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.products.ProductSearchForm;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/store")
public class ProductsController {

    @RequestMapping(value = "/categories/{id}/{categoryAlias}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable Long id, @PathVariable String categoryAlias, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/products");

        SiteActionManager.performAction("loadProductCategory", mv, request, id);

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

}
