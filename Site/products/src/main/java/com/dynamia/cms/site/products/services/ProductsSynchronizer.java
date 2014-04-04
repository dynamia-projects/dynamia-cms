/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services;

import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.StoreDTO;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.services.CrudService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class ProductsSynchronizer {

    @Autowired
    private ProductsSyncService service;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CrudService crudService;

    private LoggingService logger = new SLF4JLoggingService(ProductsSynchronizer.class);

    public void synchronize(ProductsSiteConfig siteCfg) {
        siteCfg = crudService.reload(siteCfg);

        logger.info("Starting Products Sync for " + siteCfg.getSite());

        logger.info("--Brands");
        service.synchronizeBrands(siteCfg);

        logger.info("--Stores");
        List<StoreDTO> stores = service.synchronizeStores(siteCfg);

        logger.info("--Categories");
        List<ProductCategoryDTO> categories = service.synchronizeCategories(siteCfg);
        service.disableCategoriesNoInList(siteCfg, categories);

        logger.info("--Products");
        List<ProductDTO> products = service.synchronizeProducts(siteCfg);
        for (ProductDTO productDTO : products) {
            logger.info("-- Product Details for " + productDTO.getName());
            service.syncProductDetails(siteCfg, productDTO);

            logger.info("-- Product Stock for " + productDTO.getName());
            service.syncProductStockDetails(siteCfg, productDTO);

            logger.info("-- Product Prices for " + productDTO.getName());
            service.syncProductCreditPrices(siteCfg, productDTO);
        }

        service.disableProductsNoInList(siteCfg, products);

        for (ProductDTO productDTO : products) {
            logger.info("-- Downloading Product Images for " + productDTO.getName());
            service.downloadProductImages(siteCfg, productDTO);
        }

        for (StoreDTO storeDTO : stores) {
            logger.info("-- Downloading Store Images for " + storeDTO.getName());
            service.downloadStoreImages(siteCfg, storeDTO);
        }

        service.update(siteCfg);
        logger.info("Sync Completed for " + siteCfg.getSite());

        siteService.clearCache(siteCfg.getSite());
    }

    public void synchronize(ProductsSiteConfig siteCfg, ProductDTO dto) {
        try {

            service.synchronizeProduct(siteCfg, dto);
            service.syncProductDetails(siteCfg, dto);
            service.syncProductStockDetails(siteCfg, dto);
            service.syncProductCreditPrices(siteCfg, dto);
            service.downloadProductImages(siteCfg, dto);
            siteService.clearCache(siteCfg.getSite());
        } catch (Exception e) {
            logger.error("Error Syncronizing product " + dto.getName() + " for SITE: " + siteCfg.getSite(), e);
        }
    }
}
