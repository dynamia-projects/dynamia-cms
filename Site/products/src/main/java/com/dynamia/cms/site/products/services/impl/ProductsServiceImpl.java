/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services.impl;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.query.BooleanOp;
import com.dynamia.tools.domain.query.DataPaginator;
import com.dynamia.tools.domain.query.QueryConditions;
import static com.dynamia.tools.domain.query.QueryConditions.between;
import static com.dynamia.tools.domain.query.QueryConditions.geqt;
import static com.dynamia.tools.domain.query.QueryConditions.gt;
import static com.dynamia.tools.domain.query.QueryConditions.leqt;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.util.QueryBuilder;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void generateToken(ProductsSiteConfig config) {
        config.setToken(StringUtils.randomString());
    }

    @Override
    public ProductsSiteConfig getSiteConfig(String token) {
        return crudService.findSingle(ProductsSiteConfig.class, "token", token);
    }

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

        qp.paginate(new DataPaginator(12));
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

        return crudService.find(Product.class, params);
    }

    @Override
    public List<Product> filterProducts(Site site, ProductSearchForm form) {

        QueryParameters qp = new QueryParameters();
        qp.add("active", true);

        if (form.getName() != null && !form.getName().trim().isEmpty()) {
            qp.add("name", form.getName());
        }

        if (form.getMaxPrice() != null && form.getMinPrice() == null) {
            qp.add("price", leqt(form.getMaxPrice()));
        }

        if (form.getMaxPrice() == null && form.getMinPrice() != null) {
            qp.add("price", geqt(form.getMinPrice()));
        }

        if (form.getMaxPrice() != null && form.getMinPrice() != null) {
            qp.add("price", between(form.getMinPrice(), form.getMaxPrice()));
        }
        if (form.getCategoryId() != null) {
            qp.add("category.parent.id", form.getCategoryId());
        }

        if (form.getBrandId() != null) {
            qp.add("brand.id", form.getBrandId());
        }

        if (form.isStock()) {
            qp.add("stock", gt(0));
        }

        if (form.getOrder() != null) {
            qp.orderBy(form.getOrder().getField(), form.getOrder().isAsc());
        } else {
            qp.orderBy("name", true);
        }

        if (qp.size() > 1) {
            qp.paginate(new DataPaginator(12));
            return filterProducts(site, qp);
        } else {
            return null;
        }
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
        String sql = QueryBuilder.select(ProductBrand.class, "pb")
                .where("pb.site=:site")
                .and("pb.id in (select p.brand.id from Product p where p.site = :site)")
                .orderBy("pb.name").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", site);

        return query.getResultList();
    }

    @Override
    public ProductsSiteConfig getSiteConfig(Site site) {
        return crudService.findSingle(ProductsSiteConfig.class, "site", site);
    }

    @Override
    public List<Product> find(Site site, String query) {

        QueryParameters qp = new QueryParameters();
        qp.paginate(new DataPaginator(12));
        qp.add("name", QueryConditions.like(query, true, BooleanOp.OR));
        qp.add("category.parent.name", QueryConditions.like(query, true, BooleanOp.OR));

        qp.orderBy("featured,sale,name", true);
        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> getRelatedProducts(Product product) {
        QueryParameters qp = new QueryParameters();
        QueryBuilder qb = QueryBuilder.select(Product.class, "p");
      
        if (product.getBrand() != null) {
            qp.add("brand", product.getBrand().getName());
            qb.or("p.name like :brand");
        }

        if (product.getCategory() != null && product.getCategory().getParent() != null) {
            qp.add("category", product.getCategory().getParent().getName());
            qb.or("p.name like :category");
        } else if (product.getCategory() != null) {
            qp.add("category", product.getCategory().getName());
            qb.or("p.name like :category");
        }
        
        qb.orderBy("p.price asc");
        String sql = qb.toString();
        Query query = entityManager.createQuery(sql);
        query.setMaxResults(50);
        qp.applyTo(query);
        return query.getResultList();
    }

}
