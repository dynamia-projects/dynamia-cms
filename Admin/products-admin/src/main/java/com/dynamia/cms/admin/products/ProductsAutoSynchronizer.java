/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products;

import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsSyncService;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mario
 */
@Service
public class ProductsAutoSynchronizer {

    @Autowired
    private ProductsSyncService service;
    @Autowired
    private CrudService crudService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000) //each 2 hours
    public void sync() {
        List<ProductsSiteConfig> configs = crudService.findAll(ProductsSiteConfig.class);
        for (ProductsSiteConfig config : configs) {
            service.synchronizeAll(config);
        }
    }

}
