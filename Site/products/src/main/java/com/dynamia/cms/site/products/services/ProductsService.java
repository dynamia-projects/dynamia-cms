/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mario
 */
public interface ProductsService {

    public List<ProductCategory> getCategories(Site site);

    public List<ProductCategory> getSubcategories(ProductCategory category);

    public List<Product> getProducts(ProductCategory category);

    public List<Product> getProducts(ProductBrand brand);

    public List<Product> filterProducts(Site site, Product example, Map<String, Object> params);

    public List<Product> getFeaturedProducts(Site site);

    public List<Product> getFeaturedProducts(ProductCategory category);

    public List<ProductBrand> getBrands(Site site);

    public ProductsSiteConfig getSiteConfig(Site site);

}
