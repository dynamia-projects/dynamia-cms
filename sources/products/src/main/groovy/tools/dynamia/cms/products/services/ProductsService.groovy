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
package tools.dynamia.cms.products.services

import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.ProductShareForm
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.domain.ProductCategoryDetail
import tools.dynamia.cms.products.domain.ProductDetail
import tools.dynamia.cms.products.domain.ProductReview
import tools.dynamia.cms.products.domain.ProductUserStory
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.domain.RelatedProduct
import tools.dynamia.cms.products.domain.Store
import tools.dynamia.cms.users.domain.User
import tools.dynamia.domain.query.QueryParameters

/**
 * @author Mario Serrano Leones
 */
interface ProductsService {

    List<ProductCategory> getCategories(Site site)

    List<ProductCategory> getSubcategories(ProductCategory category)

    List<Product> getProducts(ProductCategory category)

    List<Product> getProducts(ProductBrand brand)

    List<Product> filterProducts(Site site, QueryParameters params)

    List<Product> getFeaturedProducts(Site site)

    List<ProductBrand> getBrands(Site site)

    ProductsSiteConfig getSiteConfig(Site site)

    List<Product> find(Site site, String query)

    List<Product> filterProducts(Site site, ProductSearchForm form)

    List<Product> getRelatedCategoryProducts(Product product)

    void generateToken(ProductsSiteConfig config)

    ProductsSiteConfig getSiteConfig(String token)

    ProductCategory getCategoryByAlias(Site site, String alias)

    ProductBrand getBrandByAlias(Site site, String alias)

    List<ProductBrand> getBrands(ProductCategory category)

    List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand)

    List<ProductCategory> getCategories(ProductBrand brand)

    void updateViewsCount(Product product)

    List<Product> getSaleProducts(Site site)

    List<Product> getMostViewedProducts(Site site)

    List<Product> getProductsById(List<Long> ids)

    List<Product> getSpecialProducts(ProductCategory category)

    List<Product> getSpecialProducts(Site site)

    void updateProductStoryViews(Product product)

    void updateProductStoryShops(Product product)

    ProductUserStory getProductStory(Product product, User user)

    List<Product> getRecentProducts(User user)

    List<ProductDetail> getProductsDetails(List<Product> products)

    Product getProductBySku(Site site, String sku)

    Product getProductById(Site site, Long id)

    List<Store> getStores(Site site)

    List<Product> getPriceVariationsProducts(Site site)

    abstract void shareProduct(ProductShareForm form)

    List<ProductCategory> getRelatedCategories(ProductCategory category)

    ProductCategory getCategoryById(Long categoryId)

    List<ProductCategoryDetail> getCategoryDetails(ProductCategory category)

    List<RelatedProduct> getRelatedProducts(Product product, boolean requires)

    List<Product> getProducts(ProductCategory category, String orderfield, boolean asc)

    int computeProductCountByCategory(Site site)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveReview(Product product, String comment, int rate)

    ProductReview getUserReview(Product product, User user)

    @Transactional
    void computeProductStars(Product product)

    List<ProductReview> getTopReviews(Product product, int max)

    tools.dynamia.cms.products.dto.ProductsReviewResponse requestExternalReviews(ProductsSiteConfig config, String requestUuid)

    User findUserForReview(Site site, tools.dynamia.cms.products.dto.ProductsReviewResponse response)

    Product getProduct(Site site, tools.dynamia.cms.products.dto.ProductDTO dto)

    List<ProductReview> getIncompleteProductReviews(User user)

    List<ProductReview> getExternalProductReviews(Site site, tools.dynamia.cms.products.dto.ProductsReviewResponse response, User user)
}
