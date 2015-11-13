/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services;

import java.util.List;

import com.dynamia.cms.site.products.api.ProductsDatasource;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.dto.ProductBrandDTO;
import com.dynamia.cms.site.products.dto.ProductCategoryDTO;
import com.dynamia.cms.site.products.dto.ProductDTO;
import com.dynamia.cms.site.products.dto.StoreDTO;

/**
 *
 *
 * @author mario
 */
public interface ProductsSyncService {

    public List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg);

    public List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg);

    public void synchronizeBrands(ProductsSiteConfig siteCfg);

    public void synchronizeProduct(ProductsSiteConfig siteCfg, ProductDTO product);

    public void synchronizeCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO category);

    public void synchronizeBrand(ProductsSiteConfig config, ProductBrandDTO dto);

    ProductsDatasource getDatasource(ProductsSiteConfig cfg);

    void disableCategoriesNoInList(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> categories);

    void disableProductsNoInList(ProductsSiteConfig siteCfg, List<ProductDTO> products);

    public void update(ProductsSiteConfig siteCfg);

    public void synchronizeStore(ProductsSiteConfig config, StoreDTO dto);

    List<StoreDTO> synchronizeStores(ProductsSiteConfig siteCfg);

    void syncProductDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct);

    void syncProductStockDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct);

    void syncProductCreditPrices(ProductsSiteConfig siteCfg, ProductDTO remoteProduct);

    void downloadProductImages(ProductsSiteConfig siteCfg, ProductDTO product);

    void downloadStoreImages(ProductsSiteConfig siteCfg, StoreDTO store);

}
