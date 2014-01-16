/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.SearchForm;
import com.dynamia.cms.site.pages.api.SearchProvider;
import com.dynamia.cms.site.pages.api.SearchResult;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductSearchProvider implements SearchProvider {

    @Autowired
    private ProductsService service;

    @Override
    public SearchResult search(Site site, SearchForm form) {
        SearchResult rs = new SearchResult("products/productquery", false);

        System.out.println("SEARCHING PRODUCTS: " + form.getQuery());
        List<Product> products = null;
        if (form.getQuery() != null && !form.getQuery().isEmpty()) {
            products = service.find(site, form.getQuery());
        }

        if (products == null) {
            rs.addEntry("title", "Ingrese nombre del producto a buscar");
            products = service.getFeaturedProducts(site);
        } else if (!products.isEmpty()) {
            rs.addEntry("title", products.size() + " productos encontrados para busqueda '" + form.getQuery() + "'");
        } else {
            rs.addEntry("title", " No se encontraron productos para la busqueda '" + form.getQuery() + "'");
        }
        ProductsUtil.configureProductsVariable(products, rs.getEntries());
        return rs;
    }

}
