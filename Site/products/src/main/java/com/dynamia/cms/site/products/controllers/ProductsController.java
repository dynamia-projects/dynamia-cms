/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.controllers;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.query.BooleanOp;
import static com.dynamia.tools.domain.query.QueryConditions.*;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@Controller
@RequestMapping("/store")
public class ProductsController {

    @Autowired
    private ProductsService service;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CrudService crudService;

    @RequestMapping(value = "/category/{id}/{categoryAlias}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable Long id, @PathVariable String categoryAlias) {
        ModelAndView mv = new ModelAndView("products/productgrid");

        ProductCategory category = crudService.find(ProductCategory.class, id);

        List<Product> products = service.getProducts(category);
        List<ProductCategory> subcategories = service.getSubcategories(category);
        mv.addObject("title", category.getName());
        mv.addObject("prd_subcategories", subcategories);
        if (category.getParent() != null) {
            mv.addObject("prd_parentCategory", category.getParent());
        }
        ProductsUtil.configureProductsVariable(products, mv);

        return mv;

    }

    @RequestMapping("/search")
    public ModelAndView search(ProductSearchForm form, HttpServletRequest request) {
        Site site = siteService.getSite(request);
        QueryParameters qp = new QueryParameters();
        qp.add("active", true);

        if (form.getName() != null && !form.getName().trim().isEmpty()) {
            qp.add("name", form.getName());
        }

        if (form.getMaxPrice() != null && form.getMinPrice() == null) {
            qp.add("price", leqt(form.getMaxPrice()));
        }

        if (form.getMaxPrice() == null && form.getMinPrice() != null) {
            qp.add("price", geqt(form.getMinPrice()));
        }

        if (form.getMaxPrice() != null && form.getMinPrice() != null) {
            qp.add("price", between(form.getMinPrice(), form.getMaxPrice()));
        }
        if (form.getCategoryId() != null) {
            qp.add("category.parent.id", form.getCategoryId());
        }

        if (form.getBrandId() != null) {
            qp.add("brand.id", form.getBrandId());
        }

        if (form.isStock()) {
            qp.add("stock", gt(0));
        }
        List<Product> products = null;
        if (qp.size() > 1) {
            products = service.filterProducts(site, qp);
        }

        ModelAndView mv = new ModelAndView("products/productquery");
        if (products == null) {
            products = service.getFeaturedProducts(site);
            mv.addObject("title", "Ingrese los campos de busqueda");

        } else if (!products.isEmpty()) {
            mv.addObject("title", products.size() + " productos encontrados para busqueda avanzada");
        } else {
            mv.addObject("title", " No se encontraron productos para la busqueda avanzada");
        }
        ProductsUtil.configureProductsVariable(products, mv);
        mv.addObject("prd_searchForm", form);
        return mv;

    }

}
