/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
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

    private LoggingService logger = new SLF4JLoggingService(ProductsSynchronizer.class);

    public void synchronize(ProductsSiteConfig siteCfg) {
        logger.info("Starting Products Sync for " + siteCfg.getSite());
        service.synchronizeBrands(siteCfg);

        List<ProductCategoryDTO> categories = service.synchronizeCategories(siteCfg);
        service.disableCategoriesNoInList(categories);

        List<ProductDTO> products = service.synchronizeProducts(siteCfg);
        service.disableProductsNoInList(products);

        service.update(siteCfg);
        logger.info("Sync Completed for " + siteCfg.getSite());
    }
}
