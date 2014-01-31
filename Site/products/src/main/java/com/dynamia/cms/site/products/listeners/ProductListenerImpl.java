/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.listeners;

import com.dynamia.cms.site.products.api.DataChangedEvent;
import com.dynamia.cms.site.products.api.InvalidTokenException;
import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.api.ProductsListener;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.products.services.ProductsSyncService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mario
 */
public class ProductListenerImpl implements ProductsListener {

    @Autowired
    private ProductsSyncService syncService;

    @Autowired
    private ProductsService service;

    @Override
    public void productChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt);

        ProductsDatasource ds = syncService.getDatasource(config);
        ProductDTO dto = ds.getProduct(evt.getExternalRef());
        syncService.synchronizeProduct(config, dto);

    }

    @Override
    public void categoryChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt);
        ProductsDatasource ds = syncService.getDatasource(config);
        ProductCategoryDTO dto = ds.getCategory(evt.getExternalRef());
        syncService.synchronizeCategory(config, dto);

    }

    @Override

    public void brandChanged(final DataChangedEvent evt) {
        final ProductsSiteConfig config = getConfig(evt);

        ProductsDatasource ds = syncService.getDatasource(config);
        ProductBrandDTO dto = ds.getBrand(evt.getExternalRef());
        syncService.synchronizeBrand(config, dto);

    }

    private ProductsSiteConfig getConfig(DataChangedEvent evt) {
        final ProductsSiteConfig config = service.getSiteConfig(evt.getToken());
        if (config == null) {
            throw new InvalidTokenException("Cannot find ProductSiteConfig for supplied token");
        }
        return config;
    }

}
