/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services.impl;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.query.DataPaginator;
import com.dynamia.tools.domain.query.QueryConditions;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private CrudService crudService;

    @Override
    @Cacheable(value = "products", key = "'cat'+#site.key")
    public List<ProductCategory> getCategories(Site site) {
        QueryParameters qp = QueryParameters.with("site", site);
        qp.add("parent", QueryConditions.isNull());
        qp.add("active", true);
        qp.orderBy("name", true);

        return crudService.find(ProductCategory.class, qp);
    }

    @Override
    @Cacheable(value = "products", key = "#category.id")
    public List<ProductCategory> getSubcategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("site", category.getSite());
        qp.add("parent", category);
        qp.add("active", true);
        qp.orderBy("name", true);

        return crudService.find(ProductCategory.class, qp);
    }

    @Override
    public List<Product> getProducts(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("active", true);
        if (category.getParent() != null) {
            qp.add("category", category);
        } else {
            qp.add("category.parent", category);
        }

        qp.orderBy("featured, name", true);

        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> getProducts(ProductBrand brand) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("brand", brand);
        qp.orderBy("featured, name", true);

        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> filterProducts(Site site, QueryParameters params) {
        params.add("site", site);
        params.orderBy("featured, name", true);

        return crudService.find(Product.class, params);
    }

    @Override
    public List<Product> getFeaturedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("featured", true);
        qp.orderBy("featured, name", true);

        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> getFeaturedProducts(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("featured", true);
        if (category.getParent() != null) {
            qp.add("category", category);
        } else {
            qp.add("category.parent", category);
        }

        qp.orderBy("featured, name", true);

        return crudService.find(Product.class, qp);
    }

    @Override
    @Cacheable(value = "products", key = "#site.key")
    public List<ProductBrand> getBrands(Site site) {
        QueryParameters qp = QueryParameters.with("site", site);

        qp.orderBy("name", true);

        return crudService.find(ProductBrand.class, qp);
    }

    @Override
    public ProductsSiteConfig getSiteConfig(Site site) {
        return crudService.findSingle(ProductsSiteConfig.class, "site", site);
    }

    @Override
    public List<Product> find(Site site, String query) {

        QueryParameters qp = new QueryParameters();
        qp.paginate(new DataPaginator());
        qp.add("name", query);
        qp.orderBy("featured,sale,name", true);
        return crudService.find(Product.class, qp);
    }

}
