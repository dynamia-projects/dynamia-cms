/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
