/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.controllers;

import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @RequestMapping(value = "/category/{id}/{categoryAlias}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable Long id, @PathVariable String categoryAlias, Model model) {
        ModelAndView mv = new ModelAndView("products/productgrid");

        ProductCategory category = crudService.find(ProductCategory.class, id);

        if (category != null) {
            List<Product> products = service.getProducts(category);
            mv.addObject("title", category.getName());
            ProductsUtil.configureProductsVariable(products, mv);
        }

        return mv;

    }

}
