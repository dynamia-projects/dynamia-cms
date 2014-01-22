/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services.impl;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.clients.ProductsDatasourceClient;
import com.dynamia.cms.site.products.domain.ProductCategoryDetail;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDetailDTO;
import com.dynamia.cms.site.products.dto.ProductDetailDTO;
import com.dynamia.cms.site.products.services.ProductsSyncService;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityMgr;

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
            synchronizeCategory(siteCfg, remoteCategory);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO remoteCategory) {
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

        syncCategoryDetails(localCategory, remoteCategory);

        if (remoteCategory.getSubcategories() != null && !remoteCategory.getSubcategories().isEmpty()) {
            for (ProductCategoryDTO subcategory : remoteCategory.getSubcategories()) {
                synchronizeCategory(siteCfg, subcategory);
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
                synchronizeProduct(siteCfg, remoteProduct);
            } catch (Exception e) {
                logger.error("Error Synchronizing Product: " + remoteProduct.getName(), e);
            }

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
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

        crudService.save(localProduct);
        syncProductDetails(localProduct, remoteProduct);
        downloadProductImages(siteCfg, localProduct);

    }

    private void syncProductDetails(Product localProduct, ProductDTO remoteProduct) {
        deleteProductsDetails(localProduct);
        if (remoteProduct.getDetails() != null) {

            for (ProductDetailDTO remoteDetail : remoteProduct.getDetails()) {

                ProductDetail localDetail = new ProductDetail();

                localDetail.setSite(localProduct.getSite());
                localDetail.setProduct(localProduct);
                localDetail.sync(remoteDetail);
                crudService.save(localDetail);
            }
        }
    }

    private void syncCategoryDetails(ProductCategory localCategory, ProductCategoryDTO remoteCategory) {

        if (remoteCategory.getDetails() != null) {
            for (ProductCategoryDetailDTO remoteDetail : remoteCategory.getDetails()) {
                ProductCategoryDetail localDetail = crudService.findSingle(ProductCategoryDetail.class, "externalRef", remoteDetail.getExternalRef());
                if (localDetail == null) {
                    localDetail = new ProductCategoryDetail();
                }
                localDetail.setSite(localCategory.getSite());
                localDetail.setCategory(localCategory);
                localDetail.sync(remoteDetail);
                crudService.save(localDetail);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeBrands(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S BRANDS SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");

        ProductsDatasource ds = getDatasource(siteCfg);
        List<ProductBrandDTO> brands = ds.getBrands();
        for (ProductBrandDTO remoteBrand : brands) {
            synchronizeBrand(siteCfg, remoteBrand);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeBrand(ProductsSiteConfig siteCfg, ProductBrandDTO remoteBrand) {
        ProductBrand localBrand = crudService.findSingle(ProductBrand.class, "externalRef", remoteBrand.getExternalRef());
        if (localBrand == null) {
            localBrand = new ProductBrand();
            localBrand.setSite(siteCfg.getSite());
        }
        localBrand.sync(remoteBrand);

        crudService.save(localBrand);

        String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.getSite()).resolve(PRODUCTS_FOLDER + File.separator + "brands").toString();
        try {
            downloadImage(siteCfg.getDatasourceBrandImagesURL(), localBrand.getImage(), folder);
        } catch (Exception ex) {
            logger.error("Error downloading image for product's brand " + localBrand, ex);
        }
    }

    private void downloadProductImages(ProductsSiteConfig siteCfg, Product product) {
        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.getSite()).resolve(PRODUCTS_FOLDER + File.separator + "images").toString();
            downloadImage(siteCfg.getDatasourceImagesURL(), product.getImage(), folder);
            downloadImage(siteCfg.getDatasourceImagesURL(), product.getImage2(), folder);
            downloadImage(siteCfg.getDatasourceImagesURL(), product.getImage3(), folder);
            downloadImage(siteCfg.getDatasourceImagesURL(), product.getImage4(), folder);

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

    @Override
    public ProductsDatasource getDatasource(ProductsSiteConfig cfg) {
        ProductsDatasourceClient client = new ProductsDatasourceClient();
        client.setServiceURL(cfg.getDatasourceURL());
        client.setUsername(cfg.getDatasourceUsername());
        client.setPassword(cfg.getDatasourcePassword());
        return client.getProxy();
    }

    private void deleteProductsDetails(Product product) {
        if (product.getId() == null) {
            return;
        }
        List<ProductDetail> details = crudService.find(ProductDetail.class, "product", product);
        if (details != null) {
            for (ProductDetail productDetail : details) {
                crudService.delete(productDetail);
            }
        }

    }

}
