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
package tools.dynamia.cms.products.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.domain.ProductCategoryDetail
import tools.dynamia.cms.products.domain.ProductCreditPrice
import tools.dynamia.cms.products.domain.ProductDetail
import tools.dynamia.cms.products.domain.ProductStock
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.domain.RelatedProduct
import tools.dynamia.cms.products.domain.Store
import tools.dynamia.cms.products.domain.StoreContact
import tools.dynamia.cms.products.dto.ProductBrandDTO
import tools.dynamia.cms.products.dto.ProductCategoryDTO
import tools.dynamia.cms.products.dto.ProductCategoryDetailDTO
import tools.dynamia.cms.products.dto.ProductCreditPriceDTO
import tools.dynamia.cms.products.dto.ProductDTO
import tools.dynamia.cms.products.dto.ProductDetailDTO
import tools.dynamia.cms.products.dto.ProductStockDTO
import tools.dynamia.cms.products.dto.RelatedProductDTO
import tools.dynamia.cms.products.dto.StoreContactDTO
import tools.dynamia.cms.products.dto.StoreDTO
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.web.util.HttpRemotingServiceClient

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/**
 * @author Mario Serrano Leones
 */
@Service
class ProductsSyncServiceImpl implements ProductsSyncService {

    private static final String PRODUCTS_FOLDER = "products"
    private static final String STORES_FOLDER = "stores"

    private LoggingService logger = new SLF4JLoggingService(ProductsSyncService.class)

    @Autowired
    private CrudService crudService

    @PersistenceContext
    private EntityManager entityMgr

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S CATEGORIES SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        tools.dynamia.cms.products.api.ProductsDatasource ds = getDatasource(siteCfg)

        List<ProductCategoryDTO> categories = ds.getCategories(siteCfg.parametersAsMap)

        for (ProductCategoryDTO remoteCategory : categories) {
            synchronizeCategory(siteCfg, remoteCategory)
        }

        //subcategories
        for (ProductCategoryDTO remoteCategory : categories) {
            if (remoteCategory.subcategories != null && !remoteCategory.subcategories.empty) {
                for (ProductCategoryDTO subcategory : (remoteCategory.subcategories)) {
                    synchronizeCategory(siteCfg, subcategory)
                }
            }
        }

        return categories

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void synchronizeCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO remoteCategory) {
        ProductCategory localCategory = getLocalEntity(ProductCategory.class, remoteCategory.externalRef, siteCfg)
        if (localCategory == null) {
            localCategory = new ProductCategory()
            localCategory.site = siteCfg.site
        }

        localCategory.sync(remoteCategory)

        if (remoteCategory.parent != null) {
            localCategory.parent = getLocalEntity(ProductCategory.class, remoteCategory.parent.externalRef, siteCfg)
        } else {
            localCategory.parent = null
        }

        if (remoteCategory.relatedCategoryExternalRef != null) {
            localCategory.relatedCategory = getLocalEntity(ProductCategory.class, remoteCategory.relatedCategoryExternalRef, siteCfg)
        } else {
            localCategory.relatedCategory = null
        }

        crudService.save(localCategory)

        syncCategoryDetails(siteCfg, localCategory, remoteCategory)


    }

    @Override
    List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCTS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")
        tools.dynamia.cms.products.api.ProductsDatasource ds = getDatasource(siteCfg)
        List<ProductDTO> products = ds.getProducts(siteCfg.parametersAsMap)

        for (ProductDTO remoteProduct : products) {
            logger.debug("Synchronizing product. Site:  $siteCfg.site.name Name:$remoteProduct.name")
            try {
                crudService.executeWithinTransaction { synchronizeProduct(siteCfg, remoteProduct) }
            } catch (Exception e) {
                logger.error("Error Synchronizing Product: " + remoteProduct.name, e)
                e.printStackTrace()
            }
        }
        return products
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void synchronizeProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            localProduct = new Product()
            localProduct.site = siteCfg.site
        }
        localProduct.sync(remoteProduct)

        if (remoteProduct.category != null) {
            ProductCategory localCategory = getLocalEntity(ProductCategory.class,
                    remoteProduct.category.externalRef, siteCfg)

            if (localCategory != null) {
                localProduct.category = localCategory
            }
        }

        if (remoteProduct.brand != null) {
            localProduct.brand = getLocalEntity(ProductBrand.class, remoteProduct.brand.externalRef, siteCfg)
        }

        if (localProduct.category != null) {
            logger.info("Saving product $localProduct.name")
            crudService.save(localProduct)
        } else {
            logger.warn("Cannot save product $localProduct.name. Category is null")
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {

        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
            return
        }
        deleteProductsDetails(localProduct)
        if (remoteProduct.details != null) {

            for (ProductDetailDTO remoteDetail : (remoteProduct.details)) {

                ProductDetail localDetail = new ProductDetail()

                localDetail.site = localProduct.site
                localDetail.product = localProduct
                localDetail.sync(remoteDetail)
                crudService.save(localDetail)
            }
        }
    }

    private Product getLocalProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = null
        if (remoteProduct.externalRef != null) {
            localProduct = getLocalEntity(Product.class, remoteProduct.externalRef, siteCfg)
        } else if (remoteProduct.sku != null) {
            localProduct = crudService.findSingle(Product.class,
                    QueryParameters.with("sku", remoteProduct.sku).add("site", siteCfg.site))
        }
        return localProduct
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductStockDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
            return
        }
        deleteProductsStockDetails(localProduct)
        if (remoteProduct.stockDetails != null) {

            for (ProductStockDTO remoteDetail : (remoteProduct.stockDetails)) {
                try {

                    ProductStock localDetail = new ProductStock()
                    localDetail.site = localProduct.site
                    localDetail.product = localProduct
                    localDetail.store = getLocalEntity(Store.class, remoteDetail.storeExternalRef, siteCfg)
                    localDetail.sync(remoteDetail)
                    if (localDetail.product != null && localDetail.store != null) {
                        crudService.save(localDetail)
                    }
                } catch (Exception e) {
                    logger.error("Error creando stock de producto $localProduct.name, Store External ID: $remoteDetail.storeExternalRef")
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductCreditPrices(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {

        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            logger.warn(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
            return
        }
        deleteProductsCreditPrices(localProduct)
        if (remoteProduct.creditPrices != null) {

            for (ProductCreditPriceDTO remotePrice : (remoteProduct.creditPrices)) {
                if (remotePrice != null) {
                    ProductCreditPrice localPrice = new ProductCreditPrice()

                    localPrice.site = localProduct.site
                    localPrice.product = localProduct
                    localPrice.sync(remotePrice)
                    crudService.save(localPrice)
                }
            }
        }
    }

    private void syncCategoryDetails(ProductsSiteConfig siteCfg, ProductCategory localCategory,
                                     ProductCategoryDTO remoteCategory) {

        if (remoteCategory.details != null) {
            for (ProductCategoryDetailDTO remoteDetail : (remoteCategory.details)) {
                ProductCategoryDetail localDetail = getLocalEntity(ProductCategoryDetail.class,
                        remoteDetail.externalRef, siteCfg)
                if (localDetail == null) {
                    localDetail = new ProductCategoryDetail()
                }
                localDetail.site = localCategory.site
                localDetail.category = localCategory
                localDetail.sync(remoteDetail)
                crudService.save(localDetail)
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ProductBrandDTO> synchronizeBrands(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING PRODUCT'S BRANDS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        tools.dynamia.cms.products.api.ProductsDatasource ds = getDatasource(siteCfg)
        List<ProductBrandDTO> brands = ds.getBrands(siteCfg.parametersAsMap)
        for (ProductBrandDTO remoteBrand : brands) {
            synchronizeBrand(siteCfg, remoteBrand)
        }
        return brands
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void synchronizeBrand(ProductsSiteConfig siteCfg, ProductBrandDTO remoteBrand) {
        ProductBrand localBrand = getLocalEntity(ProductBrand.class, remoteBrand.externalRef, siteCfg)
        if (localBrand == null) {
            localBrand = new ProductBrand()
            localBrand.site = siteCfg.site
        }
        localBrand.sync(remoteBrand)

        crudService.save(localBrand)

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<StoreDTO> synchronizeStores(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING STORE SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        tools.dynamia.cms.products.api.ProductsDatasource ds = getDatasource(siteCfg)
        List<StoreDTO> stores = ds.getStores(siteCfg.parametersAsMap)
        for (StoreDTO remoteStroe : stores) {
            synchronizeStore(siteCfg, remoteStroe)
        }
        return stores
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void synchronizeStore(ProductsSiteConfig siteCfg, StoreDTO remoteStore) {
        Store localStore = getLocalEntity(Store.class, remoteStore.externalRef, siteCfg)
        if (localStore == null) {
            localStore = new Store()
            localStore.site = siteCfg.site
        }
        localStore.sync(remoteStore)

        crudService.save(localStore)

        if (siteCfg.syncStoreContacts && remoteStore.contacts != null) {
            for (StoreContactDTO remoteContact : (remoteStore.contacts)) {
                StoreContact localStoreContact = getLocalEntity(StoreContact.class, remoteContact.externalRef,
                        siteCfg)
                if (localStoreContact == null) {
                    localStoreContact = new StoreContact()
                    localStoreContact.site = siteCfg.site
                }
                localStoreContact.store = localStore
                localStoreContact.sync(remoteContact)
                crudService.save(localStoreContact)
            }

        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<RelatedProductDTO> synchronizeRelatedProducts(ProductsSiteConfig siteCfg) {
        logger.debug(">>>> STARTING RELATED PRODUCTS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        tools.dynamia.cms.products.api.ProductsDatasource ds = getDatasource(siteCfg)
        List<RelatedProductDTO> related = ds.getRelatedProducts(siteCfg.parametersAsMap)
        if (related != null) {
            for (RelatedProductDTO remoteRelated : related) {
                RelatedProduct localRelated = getLocalEntity(RelatedProduct.class, remoteRelated.externalRef,
                        siteCfg)
                if (localRelated == null) {
                    localRelated = new RelatedProduct()
                    localRelated.site = siteCfg.site
                    localRelated.product = getLocalEntity(Product.class, remoteRelated.productExternalRef, siteCfg)

                    if (remoteRelated.targetCategoryExternalRef != null) {
                        localRelated.targetCategory = getLocalEntity(ProductCategory.class,
                                remoteRelated.targetCategoryExternalRef, siteCfg)
                    }
                    if (remoteRelated.targetProductExternalRef != null) {
                        localRelated.targetProduct = getLocalEntity(Product.class, remoteRelated.targetProductExternalRef, siteCfg)
                    }
                }

                localRelated.sync(remoteRelated)
                if (localRelated.product != null) {
                    crudService.save(localRelated)
                }
            }
        }
        return related
    }

    @Override
    void downloadStoreImages(ProductsSiteConfig siteCfg, StoreDTO store) {
        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                    .resolve(STORES_FOLDER + File.separator + "images").toString()
            downloadImage(siteCfg.datasourceStoreImagesURL, store.image, folder)
            downloadImage(siteCfg.datasourceStoreImagesURL, store.image2, folder)
            downloadImage(siteCfg.datasourceStoreImagesURL, store.image3, folder)
            downloadImage(siteCfg.datasourceStoreImagesURL, store.image4, folder)


        } catch (Exception ex) {
            logger.error("Error downloading image from product $store.name for Site: $siteCfg.site.name", ex)
        }

        try {
            if (siteCfg.syncStoreContacts && store.contacts != null) {

                String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                        .resolve(STORES_FOLDER + File.separator + "contacts").toString()

                for (StoreContactDTO contact : (store.contacts)) {
                    logger.info("Downloading image for store contact $contact.name")
                    downloadImage(siteCfg.datasourceStoreContactImagesURL, contact.image, folder)
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    void downloadProductImages(ProductsSiteConfig siteCfg, ProductDTO product) {
        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                    .resolve(PRODUCTS_FOLDER + File.separator + "images").toString()
            downloadImage(siteCfg.datasourceImagesURL, product.image, folder)
            downloadImage(siteCfg.datasourceImagesURL, product.image2, folder)
            downloadImage(siteCfg.datasourceImagesURL, product.image3, folder)
            downloadImage(siteCfg.datasourceImagesURL, product.image4, folder)

        } catch (Exception ex) {
            logger.error("Error downloading image from product $product.name for Site: $siteCfg.site.name", ex)
        }
    }

    @Override
    void downloadBrandImages(ProductsSiteConfig siteCfg, ProductBrandDTO brand) {

        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                    .resolve(PRODUCTS_FOLDER + File.separator + "brands").toString()
            downloadImage(siteCfg.datasourceBrandImagesURL, brand.image, folder)
        } catch (Exception ex) {
            logger.error("Error downloading image for product's brand $brand", ex)
        }
    }

    @Override
    void downloadImage(String baseURL, final String imageName, final String localFolder) throws Exception {

        if (baseURL == null || baseURL.empty) {
            logger.info("-No base URL  to download images")
            return
        }

        if (imageName != null && !imageName.empty) {

            String separator = "/"
            if (baseURL.endsWith("/")) {
                separator = ""
            }

            final URL url = new URL(baseURL + separator + imageName)
            final Path folder = Paths.get(localFolder)
            final Path localFile = folder.resolve(imageName)

            if (Files.notExists(folder)) {
                Files.createDirectories(folder)
            }
            try {
                url.openStream().withCloseable {
                    Files.copy(it, localFile, StandardCopyOption.REPLACE_EXISTING)
                }
            } catch (IOException ex) {
                logger.error("-Error downloading image $imageName", ex)
            }
        } else {
            logger.info("-No image to download")
        }
    }

    @Override
    tools.dynamia.cms.products.api.ProductsDatasource getDatasource(ProductsSiteConfig cfg) {
        def client = HttpRemotingServiceClient.build(tools.dynamia.cms.products.api.ProductsDatasource.class)
        client.serviceURL = cfg.datasourceURL
        client.username = cfg.datasourceUsername
        client.password = cfg.datasourcePassword

        return client.proxy
    }

    private void deleteProductsDetails(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductDetail> details = crudService.find(ProductDetail.class, "product", product)
        if (details != null) {
            for (ProductDetail productDetail : details) {
                crudService.delete(productDetail)
            }
        }
    }

    private void deleteProductsStockDetails(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductStock> details = crudService.find(ProductStock.class, "product", product)
        if (details != null) {
            for (ProductStock stock : details) {
                crudService.delete(stock)
            }
        }
    }

    private void deleteProductsCreditPrices(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductCreditPrice> details = crudService.find(ProductCreditPrice.class, "product", product)
        if (details != null) {
            for (ProductCreditPrice price : details) {
                crudService.delete(price)
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void disableCategoriesNoInList(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> categories) {
        if (categories != null && !categories.empty) {
            List<Long> ids = new ArrayList<>()
            for (ProductCategoryDTO dto : categories) {
                ids.add(dto.externalRef)
                if (dto.subcategories != null) {
                    for (ProductCategoryDTO subdto : (dto.subcategories)) {
                        ids.add(subdto.externalRef)
                    }
                }
            }

            String sql = "update ${ProductCategory.class.simpleName} pc set active=false where pc.parent is null and pc.externalRef not in (:ids) and pc.site = :site"
            Query query = entityMgr.createQuery(sql)
            query.setParameter("ids", ids)
            query.setParameter("site", siteCfg.site)

            query.executeUpdate()

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void disableProductsNoInList(ProductsSiteConfig siteCfg, List<ProductDTO> products) {
        if (products != null && !products.empty) {
            List<Long> ids = new ArrayList<>()
            List<String> skus = new ArrayList<>()
            for (ProductDTO dto : products) {
                if (dto.externalRef != null) {
                    ids.add(dto.externalRef)
                } else if (dto.sku != null) {
                    skus.add(dto.sku)
                }
            }

            if (!ids.isEmpty()) {
                String sql = "update ${Product.class.simpleName} pc set active=false where pc.externalRef not in (:ids) and pc.site = :site"
                Query query = entityMgr.createQuery(sql)
                query.setParameter("ids", ids)
                query.setParameter("site", siteCfg.site)
                query.executeUpdate()
            }

            if (!skus.isEmpty()) {
                String sql = "update ${Product.class.simpleName} pc set active=false where pc.sku not in (:skus) and pc.site = :site"
                Query query = entityMgr.createQuery(sql)
                query.setParameter("skus", skus)
                query.setParameter("site", siteCfg.site)
                query.executeUpdate()
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void disableRelatedProductsNoInList(ProductsSiteConfig siteCfg, List<RelatedProductDTO> relatedProducts) {
        if (relatedProducts != null && !relatedProducts.empty) {
            List<Long> ids = new ArrayList<>()
            for (RelatedProductDTO dto : relatedProducts) {
                ids.add(dto.externalRef)
            }

            String sql = "update ${RelatedProduct.class.simpleName} pc set active=false where pc.externalRef not in (:ids) and pc.site = :site"
            Query query = entityMgr.createQuery(sql)
            query.setParameter("ids", ids)
            query.setParameter("site", siteCfg.site)

            query.executeUpdate()

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteStoreContactsNoInList(ProductsSiteConfig siteCfg, List<StoreDTO> stores) {
        if (siteCfg.syncStoreContacts) {
            if (stores != null && !stores.empty) {
                List<Long> ids = new ArrayList<>()
                for (StoreDTO store : stores) {
                    if (store.contacts != null && !store.contacts.empty) {
                        ids.addAll(store.contacts.collect { it.externalRef })
                    }
                }

                int r = crudService.execute("delete from StoreContact s where s.externalRef not in (:ids) and s.site = :site",
                        QueryParameters.with("site", siteCfg.site)
                                .add("ids", ids))
                logger.info(r + " store contacts deleted from stores ")
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void update(ProductsSiteConfig siteCfg) {
        siteCfg.lastSync = new Date()
        crudService.update(siteCfg)
    }

    def <T> T getLocalEntity(Class<T> clazz, Long externalRef, ProductsSiteConfig cfg) {
        if (externalRef == null) {
            return null
        }
        QueryParameters qp = QueryParameters.with("externalRef", externalRef).add("site", cfg.site)

        return crudService.findSingle(clazz, qp)
    }

}
