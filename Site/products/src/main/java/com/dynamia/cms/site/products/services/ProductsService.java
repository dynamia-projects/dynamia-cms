/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductUserStory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.tools.domain.query.QueryParameters;
import java.util.List;
import javax.servlet.http.Cookie;

/**
 *
 * @author mario
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

    public List<Product> getRelatedProducts(Product product);

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

}
