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
package tools.dynamia.cms.site.products.services;

import java.util.List;

import tools.dynamia.cms.site.products.api.ProductsDatasource;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.dto.ProductBrandDTO;
import tools.dynamia.cms.site.products.dto.ProductCategoryDTO;
import tools.dynamia.cms.site.products.dto.ProductDTO;
import tools.dynamia.cms.site.products.dto.RelatedProductDTO;
import tools.dynamia.cms.site.products.dto.StoreDTO;

/**
 *
 *
 * @author Mario Serrano Leones
 */
public interface ProductsSyncService {

    public List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg);

    public List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg);

    public List<ProductBrandDTO> synchronizeBrands(ProductsSiteConfig siteCfg);

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

    void downloadBrandImages(ProductsSiteConfig siteCfg, ProductBrandDTO store);

    List<RelatedProductDTO> synchronizeRelatedProducts(ProductsSiteConfig siteCfg);

    public void disableRelatedProductsNoInList(ProductsSiteConfig siteCfg, List<RelatedProductDTO> relatedProducts);

}
