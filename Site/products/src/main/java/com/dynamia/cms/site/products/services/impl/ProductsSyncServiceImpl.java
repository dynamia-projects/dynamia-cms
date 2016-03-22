/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.products.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.clients.ProductsDatasourceClient;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductCategoryDetail;
import com.dynamia.cms.site.products.domain.ProductCreditPrice;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductStock;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.domain.Store;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDetailDTO;
import com.dynamia.cms.site.products.dto.ProductCreditPriceDTO;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.ProductDetailDTO;
import com.dynamia.cms.site.products.dto.ProductStockDTO;
import com.dynamia.cms.site.products.dto.StoreDTO;
import com.dynamia.cms.site.products.services.ProductsSyncService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.scheduling.SchedulerUtil;
import tools.dynamia.integration.scheduling.Task;

/**
 * @author Mario Serrano Leones
 */
@Service
public class ProductsSyncServiceImpl implements ProductsSyncService {

    private static final String PRODUCTS_FOLDER = "products";
    private static final String STORES_FOLDER = "stores";

    private LoggingService logger = new SLF4JLoggingService(ProductsSyncService.class);

    @Autowired
    private CrudService crudService;

    @PersistenceContext
    private EntityManager entityMgr;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S CATEGORIES SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");

        ProductsDatasource ds = getDatasource(siteCfg);

        List<ProductCategoryDTO> categories = ds.getCategories(siteCfg.getParametersAsMap());

        for (ProductCategoryDTO remoteCategory : categories) {
            synchronizeCategory(siteCfg, remoteCategory);
        }

        return categories;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO remoteCategory) {
        ProductCategory localCategory = getLocalEntity(ProductCategory.class, remoteCategory.getExternalRef(), siteCfg);
        if (localCategory == null) {
            localCategory = new ProductCategory();
            localCategory.setSite(siteCfg.getSite());
        }

        localCategory.sync(remoteCategory);

        if (remoteCategory.getParent() != null) {
            localCategory.setParent(getLocalEntity(ProductCategory.class, remoteCategory.getParent().getExternalRef(), siteCfg));
        }else{
        	localCategory.setParent(null);
        }
        
        if (remoteCategory.getRelatedCategoryExternalRef() != null) {
            localCategory.setRelatedCategory(getLocalEntity(ProductCategory.class, remoteCategory.getRelatedCategoryExternalRef(), siteCfg));
        }else{
        	localCategory.setRelatedCategory(null);
        }

        crudService.save(localCategory);

        syncCategoryDetails(siteCfg, localCategory, remoteCategory);

        if (remoteCategory.getSubcategories() != null && !remoteCategory.getSubcategories().isEmpty()) {
            for (ProductCategoryDTO subcategory : remoteCategory.getSubcategories()) {
                synchronizeCategory(siteCfg, subcategory);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCTS SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");
        ProductsDatasource ds = getDatasource(siteCfg);
        List<ProductDTO> products = ds.getProducts(siteCfg.getParametersAsMap());

        for (ProductDTO remoteProduct : products) {
            logger.debug("Synchronizing product. Site:  " + siteCfg.getSite().getName() + " Name:" + remoteProduct.getName());
            try {
                synchronizeProduct(siteCfg, remoteProduct);
            } catch (Exception e) {
                logger.error("Error Synchronizing Product: " + remoteProduct.getName(), e);
            }
        }
        return products;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalEntity(Product.class, remoteProduct.getExternalRef(), siteCfg);
        if (localProduct == null) {
            localProduct = new Product();
            localProduct.setSite(siteCfg.getSite());
        }
        localProduct.sync(remoteProduct);

        if (remoteProduct.getCategory() != null) {
            localProduct.setCategory(getLocalEntity(ProductCategory.class, remoteProduct.getCategory().getExternalRef(), siteCfg));
        }

        if (remoteProduct.getBrand() != null) {
            localProduct.setBrand(getLocalEntity(ProductBrand.class, remoteProduct.getBrand().getExternalRef(), siteCfg));
        }

        crudService.save(localProduct);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncProductDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalEntity(Product.class, remoteProduct.getExternalRef(), siteCfg);
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product " + remoteProduct.getName() + " --> " + remoteProduct.getExternalRef());
            return;
        }
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncProductStockDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalEntity(Product.class, remoteProduct.getExternalRef(), siteCfg);
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product " + remoteProduct.getName() + " --> " + remoteProduct.getExternalRef());
            return;
        }
        deleteProductsStockDetails(localProduct);
        if (remoteProduct.getStockDetails() != null) {

            for (ProductStockDTO remoteDetail : remoteProduct.getStockDetails()) {
                try {

                    ProductStock localDetail = new ProductStock();
                    localDetail.setSite(localProduct.getSite());
                    localDetail.setProduct(localProduct);
                    localDetail.setStore(getLocalEntity(Store.class, remoteDetail.getStoreExternalRef(), siteCfg));
                    localDetail.sync(remoteDetail);
                    if (localDetail.getProduct() != null && localDetail.getStore() != null) {
                        crudService.save(localDetail);
                    }
                } catch (Exception e) {
                    logger.error("Error creando stock de producto " + localProduct.getName() + ", Store External ID: " + remoteDetail.getStoreExternalRef());
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncProductCreditPrices(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {

        Product localProduct = getLocalEntity(Product.class, remoteProduct.getExternalRef(), siteCfg);
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product " + remoteProduct.getName() + " --> " + remoteProduct.getExternalRef());
            return;
        }
        deleteProductsCreditPrices(localProduct);
        if (remoteProduct.getCreditPrices() != null) {

            for (ProductCreditPriceDTO remotePrice : remoteProduct.getCreditPrices()) {
                if (remotePrice != null) {
                    ProductCreditPrice localPrice = new ProductCreditPrice();

                    localPrice.setSite(localProduct.getSite());
                    localPrice.setProduct(localProduct);
                    localPrice.sync(remotePrice);
                    crudService.save(localPrice);
                }
            }
        }
    }

    private void syncCategoryDetails(ProductsSiteConfig siteCfg, ProductCategory localCategory, ProductCategoryDTO remoteCategory) {

        if (remoteCategory.getDetails() != null) {
            for (ProductCategoryDetailDTO remoteDetail : remoteCategory.getDetails()) {
                ProductCategoryDetail localDetail = getLocalEntity(ProductCategoryDetail.class, remoteDetail.getExternalRef(), siteCfg);
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
        List<ProductBrandDTO> brands = ds.getBrands(siteCfg.getParametersAsMap());
        for (ProductBrandDTO remoteBrand : brands) {
            synchronizeBrand(siteCfg, remoteBrand);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeBrand(ProductsSiteConfig siteCfg, ProductBrandDTO remoteBrand) {
        ProductBrand localBrand = getLocalEntity(ProductBrand.class, remoteBrand.getExternalRef(), siteCfg);
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<StoreDTO> synchronizeStores(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING STORE SYNCHRONIZATION FOR SITE " + siteCfg.getSite().getName() + " <<<<");

        ProductsDatasource ds = getDatasource(siteCfg);
        List<StoreDTO> stores = ds.getStores(siteCfg.getParametersAsMap());
        for (StoreDTO remoteStroe : stores) {
            synchronizeStore(siteCfg, remoteStroe);
        }
        return stores;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchronizeStore(ProductsSiteConfig siteCfg, StoreDTO remoteStore) {
        Store localStore = getLocalEntity(Store.class, remoteStore.getExternalRef(), siteCfg);
        if (localStore == null) {
            localStore = new Store();
            localStore.setSite(siteCfg.getSite());
        }
        localStore.sync(remoteStore);

        crudService.save(localStore);

    }

    @Override
    public void downloadStoreImages(ProductsSiteConfig siteCfg, StoreDTO store) {
        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.getSite()).resolve(STORES_FOLDER + File.separator + "images").toString();
            downloadImage(siteCfg.getDatasourceStoreImagesURL(), store.getImage(), folder);

        } catch (Exception ex) {
            logger.error("Error downloading image from product " + store.getName() + " for Site: " + siteCfg.getSite().getName(), ex);
        }
    }

    @Override
    public void downloadProductImages(ProductsSiteConfig siteCfg, ProductDTO product) {
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

            String separator = "/";
            if (baseURL.endsWith("/")) {
                separator = "";
            }

            final URL url = new URL(baseURL + separator + imageName);
            final Path folder = Paths.get(localFolder);
            final Path localFile = folder.resolve(imageName);

            if (Files.notExists(folder)) {
                Files.createDirectories(folder);
            }

            SchedulerUtil.run(new Task() {

                @Override
                public void doWork() {
                    try {
                        logger.info("Downloading image " + imageName + " to " + localFolder);
                        Files.copy(url.openStream(), localFile, StandardCopyOption.REPLACE_EXISTING);

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

    private void deleteProductsStockDetails(Product product) {
        if (product.getId() == null) {
            return;
        }
        List<ProductStock> details = crudService.find(ProductStock.class, "product", product);
        if (details != null) {
            for (ProductStock stock : details) {
                crudService.delete(stock);
            }
        }
    }

    private void deleteProductsCreditPrices(Product product) {
        if (product.getId() == null) {
            return;
        }
        List<ProductCreditPrice> details = crudService.find(ProductCreditPrice.class, "product", product);
        if (details != null) {
            for (ProductCreditPrice price : details) {
                crudService.delete(price);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void disableCategoriesNoInList(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> categories) {
        if (categories != null && !categories.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (ProductCategoryDTO dto : categories) {
                ids.add(dto.getExternalRef());
            }

            String sql = "update " + ProductCategory.class.getSimpleName() + " pc set active=false where pc.parent is null and pc.externalRef not in (:ids) and pc.site = :site";
            Query query = entityMgr.createQuery(sql);
            query.setParameter("ids", ids);
            query.setParameter("site", siteCfg.getSite());

            query.executeUpdate();

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void disableProductsNoInList(ProductsSiteConfig siteCfg, List<ProductDTO> products) {
        if (products != null && !products.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (ProductDTO dto : products) {
                ids.add(dto.getExternalRef());
            }

            String sql = "update " + Product.class.getSimpleName() + " pc set active=false where pc.externalRef not in (:ids) and pc.site = :site";
            Query query = entityMgr.createQuery(sql);
            query.setParameter("ids", ids);
            query.setParameter("site", siteCfg.getSite());

            query.executeUpdate();

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(ProductsSiteConfig siteCfg) {
        siteCfg.setLastSync(new Date());
        crudService.update(siteCfg);
    }

    public <T> T getLocalEntity(Class<T> clazz, Long externalRef, ProductsSiteConfig cfg) {
        QueryParameters qp = QueryParameters.with("externalRef", externalRef).add("site", cfg.getSite());

        return crudService.findSingle(clazz, qp);
    }

}
