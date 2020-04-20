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
package tools.dynamia.cms.products.services

import tools.dynamia.cms.products.api.ProductsDatasource
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.dto.*

/**
 *
 *
 * @author Mario Serrano Leones
 */
interface ProductsSyncService {

    List<ProductCategoryDTO> synchronizeCategories(ProductsSiteConfig siteCfg)

    List<ProductDTO> synchronizeProducts(ProductsSiteConfig siteCfg)

    void syncCategoriesDetails(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> remoteCategories)

    List<ProductBrandDTO> synchronizeBrands(ProductsSiteConfig siteCfg)

    void synchronizeProduct(ProductsSiteConfig siteCfg, ProductDTO product)

    void synchronizeCategory(ProductsSiteConfig siteCfg, ProductCategoryDTO category)

    void synchronizeBrand(ProductsSiteConfig config, ProductBrandDTO dto)

    ProductsDatasource getDatasource(ProductsSiteConfig cfg)

    void disableCategoriesNoInList(ProductsSiteConfig siteCfg, List<ProductCategoryDTO> categories)

    void disableProductsNoInList(ProductsSiteConfig siteCfg, List<ProductDTO> products)

    void deleteStoreContactsNoInList(ProductsSiteConfig siteCfg, List<StoreDTO> stores)

    void update(ProductsSiteConfig siteCfg)

    void synchronizeStore(ProductsSiteConfig config, StoreDTO dto)

    List<StoreDTO> synchronizeStores(ProductsSiteConfig siteCfg)

    void syncProductDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct)

    void syncProductStockDetails(ProductsSiteConfig siteCfg, ProductDTO remoteProduct)

    void syncProductCreditPrices(ProductsSiteConfig siteCfg, ProductDTO remoteProduct)

    void downloadProductImages(ProductsSiteConfig siteCfg, ProductDTO product)

    void downloadStoreImages(ProductsSiteConfig siteCfg, StoreDTO store)

    void downloadBrandImages(ProductsSiteConfig siteCfg, ProductBrandDTO store)

    List<RelatedProductDTO> synchronizeRelatedProducts(ProductsSiteConfig siteCfg)

    void disableRelatedProductsNoInList(ProductsSiteConfig siteCfg, List<RelatedProductDTO> relatedProducts)

    void downloadImage(String baseURL, final String imageName, final String localFolder) throws Exception

}
