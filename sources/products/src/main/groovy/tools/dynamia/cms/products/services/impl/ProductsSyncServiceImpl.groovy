/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.products.services.impl

import groovy.transform.CompileStatic
import org.apache.http.client.methods.HttpPost
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.products.api.ProductsDatasource
import tools.dynamia.cms.products.domain.*
import tools.dynamia.cms.products.dto.*
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.AbstractService
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
@CompileStatic
class ProductsSyncServiceImpl extends AbstractService implements ProductsSyncService {

    private static final String PRODUCTS_FOLDER = "products"
    private static final String STORES_FOLDER = "stores"

    private LoggingService logger = new SLF4JLoggingService(ProductsSyncService.class)


    @PersistenceContext
    private EntityManager entityMgr

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg) {
        log(">>>> STARTING PRODUCT'S CATEGORIES SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        ProductsDatasource ds = getDatasource(siteCfg)

        List<ProductCategoryDTO> categories = ds.getCategories(siteCfg.parametersAsMap)

        if (categories) {
            def fields = new HashMap()
            fields["parent"] = null
            fields["active"] = false
            crudService().batchUpdate(ProductCategory, fields, QueryParameters.with("site", siteCfg.site))

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

        crudService().save(localCategory)
    }

    @Override
    List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg) {
        log(">>>> STARTING PRODUCTS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")
        long start = System.currentTimeMillis()
        ProductsDatasource ds = getDatasource(siteCfg)
        List<ProductDTO> products = ds.getProducts(siteCfg.parametersAsMap)
        long end = System.currentTimeMillis()
        log("${products.size()} products loaded in ${end - start}ms")

        for (ProductDTO remoteProduct : products) {
            log("Synchronizing product. Site:  $siteCfg.site.name Name:$remoteProduct.name")
            try {
                crudService().executeWithinTransaction { synchronizeProduct(siteCfg, remoteProduct) }
            } catch (Exception e) {
                log("Error Synchronizing Product: " + remoteProduct.name, e)
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
            log("Saving product $localProduct.name")
            crudService().save(localProduct)
        } else {
            logWarn("Cannot save product $localProduct.name. Category is null")
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {

        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            log(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
            return
        }
        deleteProductsDetails(localProduct)
        if (remoteProduct.details != null) {

            for (ProductDetailDTO remoteDetail : (remoteProduct.details)) {

                syncProductDetail(localProduct, remoteDetail)
            }

            if (localProduct.brand) {
                def brandDetail = new ProductDetailDTO(name: siteCfg.brandLabel, value: localProduct.brand.name, order: -1, filterable: true)
                syncProductDetail(localProduct, brandDetail)
            }
        }
    }

    private void syncProductDetail(Product localProduct, ProductDetailDTO remoteDetail) {
        ProductDetail localDetail = new ProductDetail()

        localDetail.site = localProduct.site
        localDetail.product = localProduct
        localDetail.sync(remoteDetail)
        crudService().save(localDetail)
    }

    private Product getLocalProduct(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = null
        if (remoteProduct.externalRef != null) {
            localProduct = getLocalEntity(Product.class, remoteProduct.externalRef, siteCfg)
        } else if (remoteProduct.sku != null) {
            localProduct = crudService().findSingle(Product.class,
                    QueryParameters.with("sku", remoteProduct.sku).add("site", siteCfg.site))
        }
        return localProduct
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductStockDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {
        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            log(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
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
                        crudService().save(localDetail)
                    }
                } catch (Exception e) {
                    log("Error creando stock de producto $localProduct.name, Store External ID: $remoteDetail.storeExternalRef")
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncProductCreditPrices(ProductsSiteConfig siteCfg, ProductDTO remoteProduct) {

        Product localProduct = getLocalProduct(siteCfg, remoteProduct)
        if (localProduct == null) {
            log(":: Local Product is NULL - Remote Product $remoteProduct.name --> $remoteProduct.externalRef")
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
                    crudService().save(localPrice)
                }
            }
        }
    }

    @Override
    void syncCategoriesDetails(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> remoteCategories) {
        remoteCategories.each { dto ->
            ProductCategory localCat = getLocalEntity(ProductCategory, dto.externalRef, siteCfg)
            if (localCat) {
                crudService().executeWithinTransaction {
                    syncCategoryDetails(siteCfg, localCat, dto)
                }
                if (dto.subcategories) {
                    syncCategoriesDetails(siteCfg, dto.subcategories)
                }
            }
        }
    }

    private void syncCategoryDetails(ProductsSiteConfig siteCfg, ProductCategory localCategory,
                                     ProductCategoryDTO remoteCategory) {

        if (remoteCategory.details != null) {

            for (ProductCategoryDetailDTO remoteDetail : remoteCategory.details) {
                synCategoryDetail(remoteDetail, siteCfg, localCategory)
            }
            def brandDetail = new ProductCategoryDetailDTO(name: siteCfg.brandLabel, values: "", filterable: true, externalRef: -1L, order: -1)
            synCategoryDetail(brandDetail, siteCfg, localCategory)
        }
    }

    private void synCategoryDetail(ProductCategoryDetailDTO remoteDetail, ProductsSiteConfig siteCfg, ProductCategory localCategory) {
        ProductCategoryDetail localDetail = getLocalEntity(ProductCategoryDetail.class, remoteDetail.externalRef, siteCfg)
        if (localDetail == null) {
            localDetail = new ProductCategoryDetail()
        }
        println "=== SYNC CAT DETAIL $localCategory.name --> $remoteDetail.name"
        localDetail.site = localCategory.site
        localDetail.category = localCategory
        localDetail.sync(remoteDetail)
        crudService().save(localDetail)
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ProductBrandDTO> synchronizeBrands(ProductsSiteConfig siteCfg) {
        log(">>>> STARTING PRODUCT'S BRANDS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        ProductsDatasource ds = getDatasource(siteCfg)
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

        crudService().save(localBrand)

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<StoreDTO> synchronizeStores(ProductsSiteConfig siteCfg) {
        log(">>>> STARTING STORE SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        ProductsDatasource ds = getDatasource(siteCfg)
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

        crudService().save(localStore)

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
                crudService().save(localStoreContact)
            }

        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<RelatedProductDTO> synchronizeRelatedProducts(ProductsSiteConfig siteCfg) {
        log(">>>> STARTING RELATED PRODUCTS SYNCHRONIZATION FOR SITE $siteCfg.site.name <<<<")

        ProductsDatasource ds = getDatasource(siteCfg)
        List<RelatedProductDTO> related = ds.getRelatedProducts(siteCfg.parametersAsMap)
        if (related != null) {
            for (RelatedProductDTO remoteRelated : related) {
                RelatedProduct localRelated = getLocalEntity(RelatedProduct.class, remoteRelated.externalRef, siteCfg)
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
                    crudService().save(localRelated)
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
            log("Error downloading image from product $store.name for Site: $siteCfg.site.name", ex)
        }

        try {
            if (siteCfg.syncStoreContacts && store.contacts != null) {

                String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                        .resolve(STORES_FOLDER + File.separator + "contacts").toString()

                for (StoreContactDTO contact : (store.contacts)) {
                    log("Downloading image for store contact $contact.name")
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
            log("Error downloading image from product $product.name for Site: $siteCfg.site.name", ex)
        }
    }

    @Override
    void downloadBrandImages(ProductsSiteConfig siteCfg, ProductBrandDTO brand) {

        try {
            String folder = DynamiaCMS.getSitesResourceLocation(siteCfg.site)
                    .resolve(PRODUCTS_FOLDER + File.separator + "brands").toString()
            downloadImage(siteCfg.datasourceBrandImagesURL, brand.image, folder)
        } catch (Exception ex) {
            log("Error downloading image for product's brand $brand", ex)
        }
    }

    @Override
    void downloadImage(String baseURL, final String imageName, final String localFolder) throws Exception {

        if (baseURL == null || baseURL.empty) {
            log("-No base URL  to download images")
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

            if (Files.exists(localFile)) {
                log("$imageName already dowloaded from $url")
                return
            }

            try {
                url.openStream().withCloseable {
                    Files.copy(it, localFile, StandardCopyOption.REPLACE_EXISTING)
                }
            } catch (IOException ex) {
                log("-Error downloading image $imageName", ex)
            }
        } else {
            log("-No image to download")
        }
    }

    @Override
    ProductsDatasource getDatasource(ProductsSiteConfig cfg) {
        log("Creating datasource invoker for $cfg.datasourceURL")
        def invoker = new HttpInvokerProxyFactoryBean()
        invoker.serviceInterface = ProductsDatasource
        invoker.serviceUrl = cfg.datasourceURL

        def executor = new HttpComponentsHttpInvokerRequestExecutor()
        executor.readTimeout = 10 * 60000
        invoker.httpInvokerRequestExecutor = executor

        invoker.afterPropertiesSet()
        def datasource = invoker.getObject() as ProductsDatasource
        log("Datasource builded: $datasource")
        return datasource
    }

    private void deleteProductsDetails(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductDetail> details = crudService().find(ProductDetail.class, "product", product)
        if (details != null) {
            for (ProductDetail productDetail : details) {
                crudService().delete(productDetail)
            }
        }
    }

    private void deleteProductsStockDetails(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductStock> details = crudService().find(ProductStock.class, "product", product)
        if (details != null) {
            for (ProductStock stock : details) {
                crudService().delete(stock)
            }
        }
    }

    private void deleteProductsCreditPrices(Product product) {
        if (product.id == null) {
            return
        }
        List<ProductCreditPrice> details = crudService().find(ProductCreditPrice.class, "product", product)
        if (details != null) {
            for (ProductCreditPrice price : details) {
                crudService().delete(price)
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

                int r = crudService().execute("delete from StoreContact s where s.externalRef not in (:ids) and s.site = :site",
                        QueryParameters.with("site", siteCfg.site)
                                .add("ids", ids))
                log(r + " store contacts deleted from stores ")
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void update(ProductsSiteConfig siteCfg) {
        siteCfg.lastSync = new Date()
        crudService().update(siteCfg)
    }

    def <T> T getLocalEntity(Class<T> clazz, Long externalRef, ProductsSiteConfig cfg) {
        if (externalRef == null) {
            return null
        }
        QueryParameters qp = QueryParameters.with("externalRef", externalRef).add("site", cfg.site)

        return crudService().findSingle(clazz, qp)
    }

}
