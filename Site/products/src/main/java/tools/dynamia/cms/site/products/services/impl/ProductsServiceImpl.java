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
package tools.dynamia.cms.site.products.services.impl;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tools.dynamia.cms.site.core.DynamiaCMS;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.services.MailService;
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
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.domain.User;
import java.util.ArrayList;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.commons.collect.PagedList;
import static tools.dynamia.domain.query.QueryConditions.*;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.QueryBuilder;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
@Service
public class ProductsServiceImpl implements ProductsService {

    private static final String CACHE_NAME = "products";

    @Autowired
    private CrudService crudService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MailService mailService;

    @Override
    public void generateToken(ProductsSiteConfig config) {
        config.setToken(StringUtils.randomString());
    }

    @Override
    public ProductsSiteConfig getSiteConfig(String token) {
        return crudService.findSingle(ProductsSiteConfig.class, "token", token);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'cat'+#site.key+#alias")
    public ProductCategory getCategoryByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site);
        qp.add("active", true);
        qp.add("alias", QueryConditions.eq(alias));

        return crudService.findSingle(ProductCategory.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'cat'+#categoryId")
    public ProductCategory getCategoryById(Long categoryId) {
        return crudService.find(ProductCategory.class, categoryId);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'cat'+#site.key")
    public List<ProductCategory> getCategories(Site site) {
        QueryParameters qp = QueryParameters.with("site", site);
        qp.add("parent", QueryConditions.isNull());
        qp.add("active", true);
        qp.orderBy("order", true);

        return crudService.find(ProductCategory.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'subcatsOf'+#category.id")
    public List<ProductCategory> getSubcategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("site", category.getSite());
        qp.add("parent", category);
        qp.add("active", true);
        qp.orderBy("order", true);

        return crudService.find(ProductCategory.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'brandCat'+#brand.id")
    public List<ProductCategory> getCategories(ProductBrand brand) {
        String sql = QueryBuilder
                .select(ProductCategory.class, "pc")
                .where("pc.site=:site")
                .and("pc.id in (select p.category.parent.id from Product p where p.site = :site and p.brand = :brand  and p.active=true)")
                .orderBy("name").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", brand.getSite());
        query.setParameter("brand", brand);

        return query.getResultList();
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'brandCat'+#brand.id+#category.id")
    public List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand) {
        String sql = QueryBuilder
                .select(ProductCategory.class, "pc")
                .where("pc.site=:site")
                .and("pc.id in (select p.category.id from Product p where p.site = :site and p.brand = :brand and p.category.parent = :category and p.active=true)")
                .orderBy("name").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", brand.getSite());
        query.setParameter("brand", brand);
        query.setParameter("category", category);

        return query.getResultList();
    }

    @Override
    public List<Product> getProducts(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("active", true);
        String cat = "category";
        if (category.getParent() == null) {
            cat = "category.parent";
        }
        qp.add(cat, category);

        qp.paginate(getDefaultPageSize(category.getSite()));
        qp.orderBy(cat + ".name, price", true);

        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> getProducts(ProductBrand brand) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("brand", brand);
        qp.orderBy("price", true);
        qp.paginate(getDefaultPageSize(brand.getSite()));
        return crudService.find(Product.class, qp);
    }

    @Override
    public List<Product> filterProducts(Site site, QueryParameters params) {
        params.add("site", site);

        return crudService.find(Product.class, params);
    }

    @Override
    public List<Product> filterProducts(Site site, ProductSearchForm form) {

        QueryParameters params = new QueryParameters();
        params.add("active", true);

        if (form.getName() != null && !form.getName().trim().isEmpty()) {
            params.add("name", form.getName());
        }

        if (form.getMaxPrice() != null && form.getMinPrice() == null) {
            params.add("price", leqt(form.getMaxPrice()));
        }

        if (form.getMaxPrice() == null && form.getMinPrice() != null) {
            params.add("price", geqt(form.getMinPrice()));
        }

        if (form.getMaxPrice() != null && form.getMinPrice() != null) {
            params.add("price", between(form.getMinPrice(), form.getMaxPrice()));
        }

        if (form.isStock()) {
            params.add("stock", gt(0));
        }

        if (form.getOrder() != null) {
            params.orderBy(form.getOrder().getField(), form.getOrder().isAsc());
        } else {
            params.orderBy("price", true);
        }

        QueryBuilder builder = QueryBuilder.fromParameters(Product.class, "p", params);
        if (form.getBrandId() != null) {
            builder.and("p.brand.id = :brandId");
            params.add("brandId", form.getBrandId());
        }
        if (form.getCategoryId() != null) {
            builder.and("(p.category.id = :category or p.category.parent.id = :category)");
            params.add("category", form.getCategoryId());
        }

        if (form.getDetail() != null) {
            try {
                String detail[] = form.getDetail().split("=");
                builder.and(
                        "p.id in (select det.product.id from ProductDetail det where det.name = :detname and det.value = :detvalue)");
                params.add("detname", QueryConditions.eq(detail[0].trim()));
                params.add("detvalue", QueryConditions.eq(detail[1].trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (params.size() > 0) {
            params.paginate(getDefaultPageSize(site));
            return crudService.executeQuery(builder, params);
        } else {
            return null;
        }

    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'fea'+#site.key")
    public List<Product> getFeaturedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("featured", true);
        qp.paginate(getDefaultPageSize(site) + 2);
        qp.orderBy("brand.name, price", true);
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp);
        return list.getDataSource().getPageData();
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'sale'+#site.key")
    public List<Product> getSaleProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("sale", true);
        qp.paginate(getDefaultPageSize(site) + 2);
        qp.orderBy("brand.name, price", true);
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp);
        return list.getDataSource().getPageData();
    }

    @Override
    public List<Product> getMostViewedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site);
        qp.paginate(getDefaultPageSize(site) + 2);
        qp.orderBy("views", false);
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp);
        return list.getDataSource().getPageData();
    }

    @Override
    public Product getProductBySku(Site site, String sku) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("sku", sku);

        return crudService.findSingle(Product.class, qp);

    }

    @Override
    public Product getProductById(Site site, Long id) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("id", id);

        return crudService.findSingle(Product.class, qp);

    }

    @Override
    public List<Product> getProductsById(List<Long> ids) {
        QueryParameters qp = QueryParameters.with("active", true);
        qp.add("id", QueryConditions.in(ids));
        return crudService.find(Product.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#site.key")
    public List<ProductBrand> getBrands(Site site) {
        String sql = QueryBuilder
                .select(ProductBrand.class, "pb")
                .where("pb.site=:site")
                .and("pb.id in (select p.brand.id from Product p where p.site = :site  and p.active=true)")
                .orderBy("name").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", site);

        return query.getResultList();
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'brandByCat'+#category.id")
    public List<ProductBrand> getBrands(ProductCategory category) {
        String sql = QueryBuilder
                .select(ProductBrand.class, "pb")
                .where("pb.site=:site")
                .and("pb.id in (select p.brand.id from Product p where p.site = :site and ((p.category = :category or p.category.parent = :category)  and p.active=true and p.stock > 0))")
                .orderBy("name").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", category.getSite());
        query.setParameter("category", category);

        return query.getResultList();
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'brand'+#site.key+#alias")
    public ProductBrand getBrandByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site);
        qp.add("alias", QueryConditions.eq(alias));

        return crudService.findSingle(ProductBrand.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'cfg'+#site.key")
    public ProductsSiteConfig getSiteConfig(Site site) {
        return crudService.findSingle(ProductsSiteConfig.class, "site", site);
    }

    @Override
    public List<Product> find(Site site, String param) {

        QueryBuilder query = QueryBuilder
                .select(Product.class, "p")
                .where("p.active = true")
                .and("p.site = :site")
                .and("(p.name like :param or p.category.parent.name like :param "
                        + "or p.category.parent.name like :param or p.brand.name like :param "
                        + "or p.description like :param or p.sku like :param )")
                .orderBy("brand.name, p.price");

        QueryParameters qp = new QueryParameters();
        qp.add("param", param);
        qp.add("site", site);
        qp.paginate(getDefaultPageSize(site));

        return crudService.executeQuery(query, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'rld'+#product.id")
    public List<Product> getRelatedCategoryProducts(Product product) {
        QueryParameters qp = new QueryParameters();
        QueryBuilder qb = QueryBuilder.select(Product.class, "p").where("p.active = true");

        qp.add("site", product.getSite());
        qp.add("category", product.getCategory());
        qb.and("p.site = :site");

        if (product.getCategory().getParent() != null) {
            qb.and("(p.category.parent = :parentCategory or p.category = :category)");
            qp.add("parentCategory", product.getCategory().getParent());
        } else {
            qb.and("p.category = :category");
        }

        qb.orderBy("price asc");
        String sql = qb.toString();
        Query query = entityManager.createQuery(sql);
        query.setMaxResults(getDefaultPageSize(product.getSite()));
        qp.applyTo(query);
        return query.getResultList();
    }

    /**
     *
     * @param product
     * @param requires
     * @return
     */
    @Override
    public List<RelatedProduct> getRelatedProducts(Product product, boolean requires) {
        List<RelatedProduct> relateds = new ArrayList<>();

        relateds.addAll(crudService.find(RelatedProduct.class, QueryParameters.with("active", true)
                .add("targetProduct", product)
                .add("required", requires)
                .orderBy("price", false)));

        List<ProductCategory> categories = new ArrayList<>();

        if (product.getCategory() != null) {
            categories.add(product.getCategory());

            if (product.getCategory().getParent() != null) {
                categories.add(product.getCategory().getParent());
            }
        }
        if (!categories.isEmpty()) {
            relateds.addAll(crudService.find(RelatedProduct.class, QueryParameters.with("active", true)
                    .add("targetCategory", QueryConditions.in(categories))
                    .add("required", requires)
                    .orderBy("price", false)));
        }

        return relateds;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateViewsCount(Product product) {
        crudService.increaseCounter(product, "views");
    }

    private int getDefaultPageSize(Site site) {
        ProductsService self = Containers.get().findObject(ProductsService.class);
        return self.getSiteConfig(site).getProductsPerPage();
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'specialCat'+#category.id")
    public List<Product> getSpecialProducts(ProductCategory category) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
                .and("(p.sale=true or p.featured=true or p.newproduct=true)")
                .and("(p.category = :category or p.category.parent=:category)")
                .orderBy("price desc");

        QueryParameters qp = new QueryParameters();
        qp.add("category", category);
        qp.paginate(getDefaultPageSize(category.getSite()));

        return crudService.executeQuery(query, qp);

    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'priceVariations'+#site.key")
    public List<Product> getPriceVariationsProducts(Site site) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
                .and("p.site = :site")
                .and("p.sale=false and p.featured=false and p.newproduct=false")
                .and("p.price < p.lastPrice").and("p.showLastPrice = true").orderBy("price desc");

        QueryParameters qp = new QueryParameters();
        qp.add("site", site);
        qp.paginate(getDefaultPageSize(site) * 2);

        PagedList list = (PagedList) crudService.executeQuery(query, qp);
        List<Product> products = list.getDataSource().getPageData();

        return products;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'special'+#site.key")
    public List<Product> getSpecialProducts(Site site) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
                .and("(p.sale=true or p.featured=true or p.newproduct=true)").and("p.site = :site")
                .orderBy("price desc");

        QueryParameters qp = new QueryParameters();
        qp.add("site", site);
        qp.paginate(getDefaultPageSize(site));

        PagedList list = (PagedList) crudService.executeQuery(query, qp);
        return list.getDataSource().getPageData();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateProductStoryViews(Product product) {
        try {
            if (UserHolder.get().isAuthenticated()) {
                ProductUserStory story = getProductStory(product, UserHolder.get().getCurrent());
                if (story.getId() == null) {
                    story.setFirstView(new Date());
                }
                story.setLastView(new Date());
                story.setViews(story.getViews() + 1);
                crudService.save(story);
            }
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateProductStoryShops(Product product) {
        try {
            if (UserHolder.get().isAuthenticated()) {
                ProductUserStory story = getProductStory(product, UserHolder.get().getCurrent());
                if (story.getId() == null) {
                    story.setFirstShop(new Date());
                }
                story.setLastShop(new Date());
                story.setShops(story.getShops() + 1);
                crudService.save(story);
            }
        } catch (Exception e) {
        }
    }

    public ProductUserStory getProductStory(Product product, User user) {
        if (user == null) {
            return null;
        }

        QueryParameters qp = QueryParameters.with("product", product).add("user", user);

        ProductUserStory userStory = crudService.findSingle(ProductUserStory.class, qp);
        if (userStory == null) {
            userStory = new ProductUserStory();
            userStory.setProduct(product);
            userStory.setUser(user);
        }

        return userStory;
    }

    @Override
    public List<Product> getRecentProducts(User user) {
        String sql = "select s.product from ProductUserStory s where s.user = :user order by s.lastView desc";
        Query query = entityManager.createQuery(sql);
        query.setMaxResults(getDefaultPageSize(user.getSite()));
        query.setParameter("user", user);

        return query.getResultList();

    }

    @Override
    public List<ProductDetail> getProductsDetails(List<Product> products) {
        QueryParameters qp = QueryParameters.with("product", QueryConditions.in(products));
        return crudService.find(ProductDetail.class, qp);
    }

    @Override
    public List<Store> getStores(Site site) {
        QueryParameters qp = QueryParameters.with("site", site).orderBy("contactInfo.city", true);

        return crudService.find(Store.class, qp);
    }

    @Override
    public void shareProduct(ProductShareForm form) {
        MailMessage message = new MailMessage();
        Product product = getProductById(form.getSite(), form.getProductId());
        ProductsSiteConfig config = getSiteConfig(form.getSite());
        message.setTemplate(config.getShareProductMailTemplate());
        message.getTemplateModel().put("product", product);
        message.getTemplateModel().put("form", form);
        message.setTo(form.getFriendEmail());
        message.setMailAccount(config.getMailAccount());

        Path resources = DynamiaCMS.getSitesResourceLocation(product.getSite());
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage()).toFile();
            message.addAttachtment(image);
        }

        if (product.getImage2() != null && !product.getImage2().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage2()).toFile();
            message.addAttachtment(image);
        }

        if (product.getImage3() != null && !product.getImage3().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage3()).toFile();
            message.addAttachtment(image);
        }

        if (product.getImage4() != null && !product.getImage4().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage4()).toFile();
            message.addAttachtment(image);
        }

        mailService.send(message);

    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'catRelated'+#category.id")
    public List<ProductCategory> getRelatedCategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("relatedCategory", category);
        qp.add("active", true);
        qp.add("site", category.getSite());
        qp.orderBy("order", true);
        return crudService.find(ProductCategory.class, qp);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'catDetails'+#category.id")
    public List<ProductCategoryDetail> getCategoryDetails(ProductCategory category) {
        String sql = QueryBuilder
                .select(ProductCategoryDetail.class, "pcd")
                .where("pcd.site=:site")
                .and("pcd.filterable=true")
                .and("(pcd.category = :category or pcd.category.parent = :category) and pcd.name in (select pd.name from ProductDetail pd where pd.site = :site and (pd.product.category = :category or pd.product.category.parent = :category)  and pd.product.active=true)")
                .groupBy("name")
                .orderBy("order").toString();

        Query query = entityManager.createQuery(sql);
        query.setParameter("site", category.getSite());
        query.setParameter("category", category);

        List<ProductCategoryDetail> details = query.getResultList();

        String sqlValues = "select det.name, det.value from ProductDetail det inner join det.product p inner join p.category c"
                + " where (c = :category or c.parent = :category)  and p.active=true group by det.value order by det.value";
        List values = entityManager.createQuery(sqlValues).setParameter("category", category).getResultList();

        for (ProductCategoryDetail det : details) {
            for (Object objet : values) {
                Object[] catValue = (Object[]) objet;

                if (det.getName().trim().equals(catValue[0].toString().trim())) {
                    if (catValue[1] != null && !catValue[1].toString().isEmpty()) {
                        det.getCurrentValues().add(catValue[1].toString());
                    }
                }
            }
        }

        return details;
    }

}
