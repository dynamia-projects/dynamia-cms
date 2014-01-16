/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.products.services.impl;

import com.dynamia.cms.admin.products.services.ProductsSyncService;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.clients.ProductsDatasourceClient;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductDetailDTO;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.integration.scheduling.SchedulerUtil;
import com.dynamia.tools.integration.scheduling.Task;
import com.dynamia.tools.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mario
 */
@Service
public class ProductsSyncServiceImpl implements ProductsSyncService {

    private static final String PRODUCTS_FOLDER = "products";

    private LoggingService logger = new SLF4JLoggingService(ProductsSyncService.class);

    @Autowired
    private CrudService crudService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeAll(ProductsSiteConfig siteCfg) {
        synchronizeCategories(siteCfg);
        synchronizeBrands(siteCfg);
        synchronizeProducts(siteCfg);

        siteCfg.setLastSync(new Date());
        crudService.save(siteCfg);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeCategories(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S CATEGORIES SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");

        ProductsDatasource ds = getDatasource(siteCfg);

        List<ProductCategoryDTO> categories = ds.getCategories();
        for (ProductCategoryDTO remoteCategory : categories) {
            syncCategory(siteCfg, remoteCategory);
        }
    }

    private void syncCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO remoteCategory) {
        ProductCategory localCategory = crudService.findSingle(ProductCategory.class, "externalRef", remoteCategory.getExternalRef());
        if (localCategory == null) {
            localCategory = new ProductCategory();
            localCategory.setSite(siteCfg.getSite());
        }

        localCategory.sync(remoteCategory);

        if (remoteCategory.getParent() != null) {
            localCategory.setParent(crudService.findSingle(ProductCategory.class, "externalRef", remoteCategory.getParent().getExternalRef()));
        }

        crudService.save(localCategory);

        if (remoteCategory.getSubcategories() != null && !remoteCategory.getSubcategories().isEmpty()) {
            for (ProductCategoryDTO subcategory : remoteCategory.getSubcategories()) {
                syncCategory(siteCfg, subcategory);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeProducts(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCTS SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");
        ProductsDatasource ds = getDatasource(siteCfg);
        List<ProductDTO> products = ds.getProducts();
        for (ProductDTO remoteProduct : products) {
            logger.debug("Synchronizing product. Site:  " + siteCfg.getSite().getName() + " Name:" + remoteProduct.getName());
            try {
                syncProduct(siteCfg, remoteProduct);
            } catch (Exception e) {
                logger.error("Error Synchronizing Product: " + remoteProduct.getName(), e);
            }

        }
    }

    private void syncProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = crudService.findSingle(Product.class, "externalRef", remoteProduct.getExternalRef());
        if (localProduct == null) {
            localProduct = new Product();
            localProduct.setSite(siteCfg.getSite());
        }
        localProduct.sync(remoteProduct);

        if (remoteProduct.getCategory() != null) {
            localProduct.setCategory(crudService.findSingle(ProductCategory.class, "externalRef", remoteProduct.getCategory().getExternalRef()));
        }

        if (remoteProduct.getBrand() != null) {
            localProduct.setBrand(crudService.findSingle(ProductBrand.class, "externalRef", remoteProduct.getBrand().getExternalRef()));
        }

        if (localProduct.getDetails() != null) {
            for (ProductDetail detail : localProduct.getDetails()) {
                if (detail.getId() != null) {
                    crudService.delete(detail);
                }
            }
        }

        crudService.save(localProduct);

        if (remoteProduct.getDetails() != null) {
            for (ProductDetailDTO remoteDetail : remoteProduct.getDetails()) {
                ProductDetail localDetail = new ProductDetail();
                localDetail.setSite(siteCfg.getSite());
                localDetail.setProduct(localProduct);
                localDetail.sync(remoteDetail);
                crudService.save(localDetail);
            }
        }

        synchronizeProuctResources(siteCfg, localProduct);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeBrands(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S BRANDS SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");

        ProductsDatasource ds = getDatasource(siteCfg);
        List<ProductBrandDTO> brands = ds.getBrands();
        for (ProductBrandDTO remoteBrand : brands) {
            ProductBrand localBrand = crudService.findSingle(ProductBrand.class, "externalRef", remoteBrand.getExternalRef());
            if (localBrand == null) {
                localBrand = new ProductBrand();
                localBrand.setSite(siteCfg.getSite());
            }
            localBrand.sync(remoteBrand);

            crudService.save(localBrand);

            String folder = siteCfg.getSite().getResourcesLocation() + "/" + PRODUCTS_FOLDER + "/brands";
            try {
                downloadImage(siteCfg.getDatasourceBrandImagesURL(), localBrand.getImage(), folder);
            } catch (Exception ex) {
                logger.error("Error downloading image for product's brand " + localBrand, ex);
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeProuctResources(ProductsSiteConfig siteCfg, Product product) {
        try {
            String folder = siteCfg.getSite().getResourcesLocation() + "/" + PRODUCTS_FOLDER + "/images";
            downloadImage(siteCfg.getDatasourceImagesURL(), product.getImage(), folder);
        } catch (Exception ex) {
            logger.error("Error downloading image from product " + product.getName() + " for Site: " + siteCfg.getSite().getName(), ex);
        }

    }

    private void downloadImage(String baseURL, final String imageName, final String localFolder) throws Exception {

        if (imageName != null && !imageName.isEmpty()) {

            final URL url = new URL(baseURL + "/" + imageName);
            final File folder = new File(localFolder);
            final File localFile = new File(folder, imageName);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (localFile.exists()) {
                logger.info(" >Local file exists: Deleting..");
                localFile.delete();
            } else {
                localFile.createNewFile();
            }
            final FileOutputStream fos = new FileOutputStream(localFile);
            SchedulerUtil.run(new Task() {

                @Override
                public void doWork() {
                    try {
                        logger.info("Downloading image " + imageName + " to " + localFolder);

                        IOUtils.copy(url.openStream(), fos);
                    } catch (IOException ex) {
                        logger.error("Error downloading image " + imageName, ex);
                    }
                }
            });

        }

    }

    private ProductsDatasource getDatasource(ProductsSiteConfig cfg) {
        ProductsDatasourceClient client = new ProductsDatasourceClient();
        client.setServiceURL(cfg.getDatasourceURL());
        client.setUsername(cfg.getDatasourceUsername());
        client.setPassword(cfg.getDatasourcePassword());
        return client.getProxy();
    }

}
