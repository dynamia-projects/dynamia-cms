/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.integration.Containers;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
public class ProductsUtil {

    public static void configureDefaultVariables(Site site, ModelAndView mv) {
        ProductsService service = Containers.get().findObject(ProductsService.class);

        mv.addObject("prd_categories", service.getCategories(site));
        mv.addObject("prd_brands", service.getBrands(site));
        mv.addObject("prd_config", service.getSiteConfig(site));
    }

    public static void configureProductsVariable(List<Product> products, ModelAndView mv) {
        mv.addObject("prd_products", products);
    }

}
