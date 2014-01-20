/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class SearchProductsAction implements SiteAction {

    @Autowired
    private ProductsService service;

    @Override
    public String getName() {
        return "searchProducts";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        ModelAndView mv = evt.getModelAndView();
        Site site = evt.getSite();
        ProductSearchForm form = (ProductSearchForm) evt.getData();

        List<Product> products = service.filterProducts(site, form);

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

        products = ProductsUtil.setupPagination(products, evt.getRequest(), mv);
        ProductsUtil.setupProductsVar(products, mv);
        mv.addObject("prd_searchForm", form);

    }

}
