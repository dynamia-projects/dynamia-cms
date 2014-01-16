/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.services;

import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;

/**
 *
 * @author mario
 */
public interface ProductsSyncService {

    public void synchronizeAll(ProductsSiteConfig siteCfg);

    public void synchronizeCategories(ProductsSiteConfig siteCfg);

    public void synchronizeProducts(ProductsSiteConfig siteCfg);

    public void synchronizeBrands(ProductsSiteConfig siteCfg);

    public void synchronizeProuctResources(ProductsSiteConfig siteCfg, Product product);

}
