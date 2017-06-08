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

import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.products.ProductSearchForm;
import tools.dynamia.cms.site.products.ProductShareForm;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.domain.ProductCategory;
import tools.dynamia.cms.site.products.domain.ProductCategoryDetail;
import tools.dynamia.cms.site.products.domain.ProductDetail;
import tools.dynamia.cms.site.products.domain.ProductUserStory;
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.domain.RelatedProduct;
import tools.dynamia.cms.site.products.domain.Store;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.domain.query.BooleanOp;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;

/**
 *
 * @author Mario Serrano Leones
 */
public interface ProductsService {

	public List<ProductCategory> getCategories(Site site);

	public List<ProductCategory> getSubcategories(ProductCategory category);

	public List<Product> getProducts(ProductCategory category);

	public List<Product> getProducts(ProductBrand brand);

	public List<Product> filterProducts(Site site, QueryParameters params);

	public List<Product> getFeaturedProducts(Site site);

	public List<ProductBrand> getBrands(Site site);

	public ProductsSiteConfig getSiteConfig(Site site);

	List<Product> find(Site site, String query);

	public List<Product> filterProducts(Site site, ProductSearchForm form);

	public List<Product> getRelatedCategoryProducts(Product product);

	public void generateToken(ProductsSiteConfig config);

	ProductsSiteConfig getSiteConfig(String token);

	ProductCategory getCategoryByAlias(Site site, String alias);

	ProductBrand getBrandByAlias(Site site, String alias);

	public List<ProductBrand> getBrands(ProductCategory category);

	List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand);

	List<ProductCategory> getCategories(ProductBrand brand);

	public void updateViewsCount(Product product);

	public List<Product> getSaleProducts(Site site);

	public List<Product> getMostViewedProducts(Site site);

	List<Product> getProductsById(List<Long> ids);

	public List<Product> getSpecialProducts(ProductCategory category);

	List<Product> getSpecialProducts(Site site);

	void updateProductStoryViews(Product product);

	void updateProductStoryShops(Product product);

	ProductUserStory getProductStory(Product product, User user);

	List<Product> getRecentProducts(User user);

	public List<ProductDetail> getProductsDetails(List<Product> products);

	Product getProductBySku(Site site, String sku);

	Product getProductById(Site site, Long id);

	public List<Store> getStores(Site site);

	public List<Product> getPriceVariationsProducts(Site site);

	public abstract void shareProduct(ProductShareForm form);

	public List<ProductCategory> getRelatedCategories(ProductCategory category);

	public ProductCategory getCategoryById(Long categoryId);

	List<ProductCategoryDetail> getCategoryDetails(ProductCategory category);

    List<RelatedProduct> getRelatedProducts(Product product, boolean requires);

	List<Product> getProducts(ProductCategory category, String orderfield, boolean asc);

	int computeProductCountByCategory(Site site);

}
