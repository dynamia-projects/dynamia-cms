/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.services.ProductsService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
@CMSExtension
public class ProductsGridPageType implements PageTypeExtension {

    @Autowired
    private ProductsService service;

    @Override
    public String getName() {
        return "Products Grid";
    }

    @Override
    public String getViewName() {
        return "products/products";
    }

    @Override
    public String getDescriptorId() {
        return "ProductGridConfig";
    }

    @Override
    public Map<String, Object> setupPage(Page page) {
        Map<String, Object> map = new HashMap<>();
        ProductsUtil.setupProductsVar(service.getFeaturedProducts(page.getSite()), map);
        return map;
    }

}
