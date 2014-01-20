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
import com.dynamia.tools.commons.collect.PagedListDataSource;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;
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
@RequestMapping("/store")
public class ProductsController {

    private static final String DATASOURCE = "productsDatasource";

    @Autowired
    private ProductsService service;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CrudService crudService;

    @RequestMapping(value = "/category/{id}/{categoryAlias}", method = RequestMethod.GET)
    public ModelAndView category(@PathVariable Long id, @PathVariable String categoryAlias, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/products");

        ProductCategory category = crudService.find(ProductCategory.class, id);

        List<Product> products = service.getProducts(category);
        List<ProductCategory> subcategories = service.getSubcategories(category);
        mv.addObject("title", category.getName());
        mv.addObject("prd_subcategories", subcategories);
        if (category.getParent() != null) {
            mv.addObject("prd_parentCategory", category.getParent());
        }

        products = ProductsUtil.setupPagination(products, request, mv);
        ProductsUtil.setupProductsVar(products, mv);

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

        Site site = siteService.getSite(request);

        List<Product> products = service.filterProducts(site, form);

        ModelAndView mv = new ModelAndView("products/productquery");
        if (products == null) {
            products = service.getFeaturedProducts(site);
            mv.addObject("title", "Ingrese los campos de busqueda");
        } else if (!products.isEmpty()) {
            mv.addObject("title", products.size() + " productos encontrados para busqueda avanzada");
        } else {
            mv.addObject("title", " No se encontraron productos para la busqueda avanzada");
        }

        if (form.getCategoryId() != null) {
            ProductCategory category = new ProductCategory();
            category.setId(form.getCategoryId());
            List<ProductCategory> subcategories = service.getSubcategories(category);
            mv.addObject("prd_subcategories", subcategories);            
        }

        products = ProductsUtil.setupPagination(products, request, mv);
        ProductsUtil.setupProductsVar(products, mv);
        mv.addObject("prd_searchForm", form);
        return mv;
    }

    @RequestMapping("/products/page/{page}")
    public ModelAndView paginate(@PathVariable int page, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("products/productquery");
        PagedListDataSource datasource = (PagedListDataSource) request.getSession().getAttribute(DATASOURCE);

        List<Product> products = null;
        if (datasource != null) {
            datasource.setActivePage(page);
            products = datasource.getPageData();
            mv.addObject(DATASOURCE, datasource);
        } else {
            Site site = siteService.getSite(request);
            products = service.getFeaturedProducts(site);
            products = ProductsUtil.setupPagination(products, request, mv);
        }
        ProductsUtil.setupProductsVar(products, mv);

        return mv;

    }

    @RequestMapping("/products/{id}")
    public ModelAndView product(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("products/product");
        Product product = crudService.find(Product.class, id);

        if (product != null) {
            List<Product> relatedProducts = service.getRelatedProducts(product);

            ProductsUtil.setupProductVar(product, mv);
            ProductsUtil.setupProductsVar(relatedProducts, mv);

        }
        return mv;

    }

}
