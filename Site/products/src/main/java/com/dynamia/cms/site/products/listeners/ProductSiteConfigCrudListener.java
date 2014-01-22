/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dynamia.cms.site.products.listeners;

import com.dynamia.cms.site.core.api.CMSListener;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.util.CrudServiceListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
@CMSListener
public class ProductSiteConfigCrudListener extends CrudServiceListenerAdapter<ProductsSiteConfig> {

    @Autowired
    private ProductsService service;
    
    @Override
    public void beforeCreate(ProductsSiteConfig config) {
        service.generateToken(config);
    }
    
    
    
}
