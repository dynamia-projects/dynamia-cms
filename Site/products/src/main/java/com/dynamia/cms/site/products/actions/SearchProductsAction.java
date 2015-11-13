/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario
 */
@CMSAction
public class SearchProductsAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    @Override
    public String getName() {
        return "searchProducts";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        Site site = evt.getSite();

        List<Product> products = null;
        if (evt.getData() instanceof String) {
            String query = (String) evt.getData();
            products = service.find(site, query);
        } else if (evt.getData() instanceof ProductSearchForm) {
            ProductSearchForm form = (ProductSearchForm) evt.getData();
            products = service.filterProducts(site, form);
            mv.addObject("prd_searchForm", form);
            if (form.getCategoryId() != null) {
                ProductCategory category = crudService.find(ProductCategory.class, form.getCategoryId());
                List<ProductCategory> subcategories = service.getSubcategories(category);

                mv.addObject("prd_category", category);
                mv.addObject("prd_parentCategory", category.getParent());
                mv.addObject("prd_subcategories", subcategories);
            }
        }

        if (products == null) {
            products = service.getFeaturedProducts(site);
            mv.addObject("title", "Ingrese los campos de busqueda");
        } else if (!products.isEmpty()) {
            mv.addObject("title", products.size() + " productos encontrados");
        } else {
            mv.addObject("title", " No se encontraron productos para la busqueda avanzada");
        }

        products = ProductsUtil.setupPagination(products, evt.getRequest(), mv);
        ProductsUtil.setupProductsVar(products, mv);

    }

}
