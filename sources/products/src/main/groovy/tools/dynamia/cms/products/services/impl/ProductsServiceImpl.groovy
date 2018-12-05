/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.products.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.remoting.RemoteConnectFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.cms.products.ProductSearchForm
import tools.dynamia.cms.products.ProductShareForm
import tools.dynamia.cms.products.api.ProductReviewsConnector
import tools.dynamia.cms.products.domain.*
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.StringUtils
import tools.dynamia.commons.collect.PagedList
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.jpa.JpaQuery
import tools.dynamia.domain.query.BooleanOp
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.AbstractService
import tools.dynamia.domain.util.QueryBuilder
import tools.dynamia.integration.Containers
import tools.dynamia.web.util.HttpRemotingServiceClient

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import java.nio.file.Path

import static tools.dynamia.domain.query.QueryConditions.*

/**
 * @author Mario Serrano Leones
 */
@Service
@CompileStatic
class ProductsServiceImpl extends AbstractService implements ProductsService {

    private static final String CACHE_NAME = "products"


    @PersistenceContext
    private EntityManager entityManager

    @Autowired
    private MailService mailService

    @Autowired
    private UserService userService

    @Override
    void generateToken(ProductsSiteConfig config) {
        config.token = StringUtils.randomString()
    }

    @Override
    ProductsSiteConfig getSiteConfig(String token) {
        return crudService().findSingle(ProductsSiteConfig.class, "token", token)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#site.key+#alias")
    ProductCategory getCategoryByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("active", true)
        qp.add("alias", QueryConditions.eq(alias))

        return crudService().findSingle(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#categoryId")
    ProductCategory getCategoryById(Long categoryId) {
        return crudService().find(ProductCategory.class, categoryId)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#site.key")
    List<ProductCategory> getCategories(Site site) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("parent", QueryConditions.isNull())
        qp.add("active", true)
        qp.orderBy("order", true)

        return crudService().find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'subcatsOf'+#category.id")
    List<ProductCategory> getSubcategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("site", category.site)
        qp.add("parent", category)
        qp.add("active", true)
        qp.orderBy("order", true)

        return crudService().find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandCat'+#brand.id")
    List<ProductCategory> getCategories(ProductBrand brand) {
        String sql = QueryBuilder.select(ProductCategory.class, "pc").where("pc.site=:site").and(
                "pc.id in (select p.category.parent.id from Product p where p.site = :site and p.brand = :brand  and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", brand.site)
        query.setParameter("brand", brand)

        return query.resultList
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandCat'+#brand.id+#category.id")
    List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand) {
        String sql = QueryBuilder.select(ProductCategory.class, "pc").where("pc.site=:site").and(
                "pc.id in (select p.category.id from Product p where p.site = :site and p.brand = :brand and p.category.parent = :category and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", brand.site)
        query.setParameter("brand", brand)
        query.setParameter("category", category)

        return query.resultList
    }

    @Override
    List<Product> getProducts(ProductCategory category) {
        return getProducts(category, "price", true)
    }

    @Override
    List<Product> getProducts(ProductCategory category, String orderfield, boolean asc) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", category.site)

        if (category.parent == null) {
            qp.addGroup(QueryParameters.with("category.parent", QueryConditions.eq(category, BooleanOp.OR))
                    .add("category", QueryConditions.eq(category, BooleanOp.OR)), BooleanOp.AND)
        } else {
            qp.add("category", category)
        }

        qp.paginate(getDefaultPageSize(category.site))
        qp.orderBy(orderfield, asc)

        return crudService().find(Product.class, qp)
    }

    @Override
    List<Product> getProducts(ProductBrand brand) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("brand", brand)
        qp.orderBy("price", true)
        qp.paginate(getDefaultPageSize(brand.site))
        return crudService().find(Product.class, qp)
    }

    @Override
    List<Product> filterProducts(Site site, QueryParameters params) {
        params.add("site", site)

        return crudService().find(Product.class, params)
    }

    @Override
    List<Product> filterProducts(Site site, ProductSearchForm form) {

        QueryParameters params = new QueryParameters()
        params.add("active", true)
        params.add("site", site)

        if (form.name != null && !form.name.trim().empty) {
            params.add("name", form.name)
        }

        if (form.maxPrice != null && form.minPrice == null) {
            params.add("price", leqt(form.maxPrice))
        }

        if (form.maxPrice == null && form.minPrice != null) {
            params.add("price", geqt(form.minPrice))
        }

        if (form.maxPrice != null && form.minPrice != null) {
            params.add("price", between(form.minPrice, form.maxPrice))
        }

        if (form.stock) {
            params.add("stock", gt(0))
        }

        if (form.order != null) {
            params.orderBy(form.order.field, form.order.asc)
        } else {
            params.orderBy("price", true)
        }

        QueryBuilder builder = QueryBuilder.fromParameters(Product.class, "p", params)
        if (form.brandId != null) {
            builder.and("p.brand.id = :brandId")
            params.add("brandId", form.brandId)
        }
        if (form.categoryId != null) {
            builder.and("(p.category.id = :category or p.category.parent.id = :category)")
            params.add("category", form.categoryId)
        }

        Map<String, String> map = new HashMap<>()
        filterByDetail(form.detail, "1", params, builder, map)
        filterByDetail(form.detail2, "2", params, builder, map)
        filterByDetail(form.detail3, "3", params, builder, map)
        filterByDetail(form.detail4, "4", params, builder, map)

        form.setAttribute("filteredDetails", map)


        if (params.size() > 0) {
            params.paginate(getDefaultPageSize(site))
            return crudService().executeQuery(builder, params)
        } else {
            return null
        }

    }

    private String filterByDetail(String det, String suffix, QueryParameters params, QueryBuilder builder, Map<String, String> map) {
        try {
            if (det != null && !det.empty && det.contains(";")) {
                String[] detail = det.split(";")
                String name = "detname" + suffix
                String value = "detvalue" + suffix
                builder.and(
                        "p.id in (select det.product.id from ProductDetail det where det.name = :" + name + " and det.value = :" + value + ")")
                params.add(name, QueryConditions.eq(detail[0].trim()))
                params.add(value, QueryConditions.eq(detail[1].trim()))
                map.put(detail[0], detail[1])
                return detail[0].trim()
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return null
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'fea'+#site.key")
    List<Product> getFeaturedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("featured", true)
        qp.paginate(getDefaultPageSize(site) + 2)
        qp.orderBy("brand.name, price", true)
        PagedList<Product> list = (PagedList<Product>) crudService().find(Product.class, qp)
        return list.dataSource.pageData
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'sale'+#site.key")
    List<Product> getSaleProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("sale", true)
        qp.paginate(getDefaultPageSize(site) + 2)
        qp.orderBy("brand.name, price", true)
        PagedList<Product> list = (PagedList<Product>) crudService().find(Product.class, qp)
        return list.dataSource.pageData
    }

    @Override
    List<Product> getMostViewedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site)
        qp.paginate(getDefaultPageSize(site) + 2)
        qp.orderBy("views", false)
        PagedList<Product> list = (PagedList<Product>) crudService().find(Product.class, qp)
        return list.dataSource.pageData
    }

    @Override
    Product getProductBySku(Site site, String sku) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("sku", sku)

        return crudService().findSingle(Product.class, qp)

    }

    @Override
    Product getProductById(Site site, Long id) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("id", id)

        return crudService().findSingle(Product.class, qp)

    }

    @Override
    List<Product> getProductsById(List<Long> ids) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("id", QueryConditions.in(ids))
        return crudService().find(Product.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "#site.key")
    List<ProductBrand> getBrands(Site site) {
        String sql = QueryBuilder.select(ProductBrand.class, "pb").where("pb.site=:site")
                .and("pb.id in (select p.brand.id from Product p where p.site = :site  and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", site)

        return query.resultList
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandByCat'+#category.id")
    List<ProductBrand> getBrands(ProductCategory category) {
        String sql = QueryBuilder.select(ProductBrand.class, "pb").where("pb.site=:site").and(
                "pb.id in (select p.brand.id from Product p where p.site = :site and ((p.category = :category or p.category.parent = :category)  and p.active=true and p.stock > 0))")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", category.site)
        query.setParameter("category", category)

        return query.resultList
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brand'+#site.key+#alias")
    ProductBrand getBrandByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("alias", QueryConditions.eq(alias))

        return crudService().findSingle(ProductBrand.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cfg'+#site.key")
    ProductsSiteConfig getSiteConfig(Site site) {
        return crudService().findSingle(ProductsSiteConfig.class, "site", site)
    }

    @Override
    List<Product> find(Site site, String param) {
        if (param == null || param.empty) {
            return Collections.EMPTY_LIST
        }

        List<Long> idCategories = crudService().executeQuery("select c.id from ProductCategory c where c.site = :site and c.active = true and c.parent is null and (c.name like :param or c.tags like :param)",
                QueryParameters.with("site", site).add("param", param))
        String cats = ""
        if (!idCategories.empty) {
            cats = " or p.category.parent.id in (:idsCategories) "
        }

        QueryBuilder query = QueryBuilder.select(Product.class, "p")
                .leftJoin("p.brand brd").where("p.active = true").and("p.site = :site")
                .and("(p.name like :param or p.category.name like :param or p.category.tags like :param or brd.name like :param "
                + "or p.description like :param or p.sku like :param $cats )")
                .orderBy("p.price")

        QueryParameters qp = new QueryParameters()
        qp.add("param", param)
        qp.add("site", site)
        if (!idCategories.empty) {
            qp.add("idsCategories", idCategories)
        }
        qp.paginate(getDefaultPageSize(site))

        return crudService().executeQuery(query, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'rld'+#product.id")
    List<Product> getRelatedCategoryProducts(Product product) {
        QueryParameters qp = new QueryParameters()
        QueryBuilder qb = QueryBuilder.select(Product.class, "p").where("p.active = true")

        qp.add("site", product.site)
        qp.add("category", product.category)
        qb.and("p.site = :site")

        if (product.category.parent != null) {
            qb.and("(p.category.parent = :parentCategory or p.category = :category)")
            qp.add("parentCategory", product.category.parent)
        } else {
            qb.and("p.category = :category")
        }

        qb.orderBy("price asc")
        String sql = qb.toString()
        Query query = entityManager.createQuery(sql)
        query.maxResults = getDefaultPageSize(product.site)
        qp.applyTo(new JpaQuery(query))
        return query.resultList
    }

    /**
     * @param product
     * @param requires
     * @return
     */
    @Override
    List<RelatedProduct> getRelatedProducts(Product product, boolean requires) {
        List<RelatedProduct> relateds = new ArrayList<>()

        relateds.addAll(crudService().find(RelatedProduct.class, QueryParameters.with("active", true)
                .add("targetProduct", product).add("required", requires).orderBy("price", false)))

        List<ProductCategory> categories = new ArrayList<>()

        if (product.category != null) {
            categories.add(product.category)

            if (product.category.parent != null) {
                categories.add(product.category.parent)
            }
        }
        if (!categories.isEmpty()) {
            relateds.addAll(crudService().find(RelatedProduct.class,
                    QueryParameters.with("active", true).add("targetCategory", QueryConditions.in(categories))
                            .add("required", requires).orderBy("price", false)))
        }

        return relateds
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateViewsCount(Product product) {
        crudService().updateField(product, "views", product.views + 1L)
    }

    private int getDefaultPageSize(Site site) {
        ProductsService self = Containers.get().findObject(ProductsService.class)
        ProductsSiteConfig config = self.getSiteConfig(site)
        if (config != null) {
            return config.productsPerPage
        } else {
            return 40
        }
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'specialCat'+#category.id")
    List<Product> getSpecialProducts(ProductCategory category) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
                .and("(p.sale=true or p.featured=true or p.newproduct=true)")
                .and("(p.category = :category or p.category.parent=:category)").orderBy("price desc")

        QueryParameters qp = new QueryParameters()
        qp.add("category", category)
        qp.paginate(getDefaultPageSize(category.site))

        return crudService().executeQuery(query, qp)

    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'priceVariations'+#site.key")
    List<Product> getPriceVariationsProducts(Site site) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true").and("p.site = :site")
                .and("p.sale=false and p.featured=false and p.newproduct=false").and("p.price < p.lastPrice")
                .and("p.showLastPrice = true").orderBy("price desc")

        QueryParameters qp = new QueryParameters()
        qp.add("site", site)
        qp.paginate(getDefaultPageSize(site) * 2)

        PagedList list = (PagedList) crudService().executeQuery(query, qp)
        List<Product> products = list.dataSource.pageData

        return products
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'special'+#site.key")
    List<Product> getSpecialProducts(Site site) {
        QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
                .and("(p.sale=true or p.featured=true or p.newproduct=true)").and("p.site = :site")
                .orderBy("price desc")

        QueryParameters qp = new QueryParameters()
        qp.add("site", site)
        qp.paginate(getDefaultPageSize(site))

        PagedList list = (PagedList) crudService().executeQuery(query, qp)
        return list.dataSource.pageData
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void updateProductStoryViews(Product product) {
        try {
            if (UserHolder.get().authenticated) {
                ProductUserStory story = getProductStory(product, UserHolder.get().current)
                if (story.id == null) {
                    story.firstView = new Date()
                }
                story.lastView = new Date()
                story.views = story.views + 1
                crudService().save(story)
            }
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void updateProductStoryShops(Product product) {
        try {
            if (UserHolder.get().authenticated) {
                ProductUserStory story = getProductStory(product, UserHolder.get().current)
                if (story.id == null) {
                    story.firstShop = new Date()
                }
                story.lastShop = new Date()
                story.shops = story.shops + 1
                crudService().save(story)
            }
        } catch (Exception e) {
        }
    }

    @Override
    ProductUserStory getProductStory(Product product, User user) {
        if (user == null) {
            return null
        }

        QueryParameters qp = QueryParameters.with("product", product).add("user", user)

        ProductUserStory userStory = crudService().findSingle(ProductUserStory.class, qp)
        if (userStory == null) {
            userStory = new ProductUserStory()
            userStory.product = product
            userStory.user = user
        }

        return userStory
    }

    @Override
    List<Product> getRecentProducts(User user) {
        String sql = "select s.product from ProductUserStory s where s.user = :user order by s.lastView desc"
        Query query = entityManager.createQuery(sql)
        query.maxResults = getDefaultPageSize(user.site)
        query.setParameter("user", user)

        return query.resultList

    }

    @Override
    List<ProductDetail> getProductsDetails(List<Product> products) {
        QueryParameters qp = QueryParameters.with("product", QueryConditions.in(products))
        return crudService().find(ProductDetail.class, qp)
    }

    @Override
    List<Store> getStores(Site site) {
        QueryParameters qp = QueryParameters.with("site", site).orderBy("contactInfo.city", true)

        return crudService().find(Store.class, qp)
    }

    @Override
    void shareProduct(ProductShareForm form) {
        MailMessage message = new MailMessage()
        Product product = getProductById(form.site, form.productId)
        ProductsSiteConfig config = getSiteConfig(form.site)
        message.template = config.shareProductMailTemplate
        message.templateModel.put("product", product)
        message.templateModel.put("form", form)
        message.to = form.friendEmail
        message.mailAccount = config.mailAccount

        Path resources = DynamiaCMS.getSitesResourceLocation(product.site)
        if (product.image != null && !product.image.empty) {
            File image = resources.resolve("products/images/" + product.image).toFile()
            message.addAttachtment(image)
        }

        if (product.image2 != null && !product.image2.empty) {
            File image = resources.resolve("products/images/" + product.image2).toFile()
            message.addAttachtment(image)
        }

        if (product.image3 != null && !product.image3.empty) {
            File image = resources.resolve("products/images/" + product.image3).toFile()
            message.addAttachtment(image)
        }

        if (product.image4 != null && !product.image4.empty) {
            File image = resources.resolve("products/images/" + product.image4).toFile()
            message.addAttachtment(image)
        }

        mailService.send(message)

    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'catRelated'+#category.id")
    List<ProductCategory> getRelatedCategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("relatedCategory", category)
        qp.add("active", true)
        qp.add("site", category.site)
        qp.orderBy("order", true)
        return crudService().find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'catDetails'+#category.id")
    List<ProductCategoryDetail> getCategoryDetails(ProductCategory category) {
        String sql = QueryBuilder.select(ProductCategoryDetail.class, "pcd").where("pcd.site=:site")
                .and("pcd.filterable=true")
                .and("(pcd.category = :category or pcd.category.parent = :category) and pcd.name in (select pd.name from ProductDetail pd where pd.site = :site and (pd.product.category = :category or pd.product.category.parent = :category)  and pd.product.active=true)")
                .groupBy("name").orderBy("order").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", category.site)
        query.setParameter("category", category)

        List<ProductCategoryDetail> details = query.resultList


        String sqlValues = "select det.name, det.value from ProductDetail det inner join det.product p inner join p.category c where (c = :category or c.parent = :category)  and p.active=true group by det.value order by det.value"
        List values = entityManager.createQuery(sqlValues).setParameter("category", category).resultList

        for (ProductCategoryDetail det : details) {
            for (Object objet : values) {
                Object[] catValue = (Object[]) objet

                if (det.name.trim().equals(catValue[0].toString().trim())) {
                    if (catValue[1] != null && !catValue[1].toString().empty) {
                        det.currentValues.add(catValue[1].toString())
                    }
                }
            }
        }

        return details
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    int computeProductCountByCategory(Site site) {
        if (site != null) {
            log("Computing products count from Site " + site)
            String sql = "update ProductCategory pc set pc.productsCount = (select count(p) from Product p where p.active=true and p.site = :site and p.category.id = pc.id) where pc.site = :site and pc.parent is not  null"
            int result = entityManager.createQuery(sql).setParameter("site", site).executeUpdate()
            log(result + " subcategories updated")

            return result
        }
        return 0
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveReview(Product product, String comment, int rate) {

        User user = UserHolder.get().current

        ProductReview rev = getUserReview(product, user)

        if (rev != null && !rev.incomplete) {
            throw new ValidationError("Ya has enviado una reseña sobre este producto")
        } else if (rev == null) {
            rev = new ProductReview()
        }

        rev.user = user
        rev.product = product
        rev.site = product.site
        rev.comment = comment
        rev.stars = rate
        rev.incomplete = false
        crudService().save(rev)
    }

    @Override
    ProductReview getUserReview(Product product, User user) {
        return crudService().findSingle(ProductReview.class, QueryParameters.with("product", product).add("user", user))
    }

    @Override
    List<ProductReview> getIncompleteProductReviews(User user) {
        List<ProductReview> reviews = crudService().find(ProductReview.class, QueryParameters.with("user", user).add("incomplete", true))

        return reviews
    }

    @Override
    @Transactional
    void computeProductStars(Product product) {
        product = crudService().find(Product.class, product.id)

        Double stars = crudService().executeProjection(Double.class,
                "select avg(r.stars) from ProductReview r where r.product = :product and r.incomplete=false",
                QueryParameters.with("product", product))

        if (stars == null) {
            stars = 0.0d
        }

        product.reviews = crudService().count(ProductReview.class, QueryParameters.with("product", product))
        product.stars1Count = crudService().count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 1))
        product.stars2Count = crudService().count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 2))
        product.stars3Count = crudService().count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 3))
        product.stars4Count = crudService().count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 4))
        product.stars5Count = crudService().count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 5))

        if (stars != null && stars > 0) {
            product.stars = stars
        } else {
            product.stars = 0.0d
        }

        crudService().update(product)
    }

    @Override
    List<ProductReview> getTopReviews(Product product, int max) {
        List<ProductReview> result = crudService().find(ProductReview.class, QueryParameters.with("product", product)
                .add("incomplete", false).orderBy("creationDate", false).paginate(max))

        if (result instanceof PagedList) {
            result = ((PagedList) result).dataSource.pageData
        }

        return result
    }

    @Override
    tools.dynamia.cms.products.dto.ProductsReviewResponse requestExternalReviews(ProductsSiteConfig config, String requestUuid) {
        if (config != null && config.reviewsConnectorURL != null && !config.reviewsConnectorURL.empty) {
            ProductReviewsConnector connector = HttpRemotingServiceClient.build(ProductReviewsConnector.class)
                    .setServiceURL(config.reviewsConnectorURL)
                    .getProxy()

            if (connector != null) {
                try {
                    return connector.requestReviews(requestUuid)
                } catch (RemoteConnectFailureException e) {
                    return tools.dynamia.cms.products.dto.ProductsReviewResponse.rejected("No hay conexion con servicio")
                }
            }
        }

        return tools.dynamia.cms.products.dto.ProductsReviewResponse.rejected()
    }

    @Override
    User findUserForReview(Site site, tools.dynamia.cms.products.dto.ProductsReviewResponse response) {
        String email = response.email

        if (email.contains(",")) {
            try {
                email = email.split(",")[0]
                email = email.trim()
            } catch (Exception e) {

            }
        }

        User user = userService.getUser(site, email, response.identification)
        if (user == null) {
            user = userService.getUser(site, email)
        }


        if (user == null) {
            user = new User()
            user.site = site
            user.username = email
            user.firstName = response.name
            user.lastName = response.lastName
            user.enabled = true
            user.contactInfo.address = response.address
            user.contactInfo.city = response.city
            user.contactInfo.email = response.email
            user.contactInfo.country = response.country
            user.contactInfo.mobileNumber = response.mobileNumber
            user.contactInfo.phoneNumber = response.phoneNumber
            user.identification = response.identification
            user.externalRef = response.externalRef
            userService.setupPassword(user, response.identification)
            user = crudService().create(user)
        }
        return user
    }

    @Override
    Product getProduct(Site site, tools.dynamia.cms.products.dto.ProductDTO dto) {
        Product product = null

        if (dto != null) {
            if (dto.externalRef != null) {
                product = crudService().findSingle(Product.class, QueryParameters.with("site", site).add("active", true)
                        .add("externalRef", dto.externalRef))
            }

            if (product == null && dto.sku != null && !dto.sku.empty) {
                product = crudService().findSingle(Product.class, QueryParameters.with("site", site).add("active", true)
                        .add("sku", dto.sku).setAutocreateSearcheableStrings(false))
            }
        }

        return product
    }

    @Override
    List<ProductReview> getExternalProductReviews(Site site, tools.dynamia.cms.products.dto.ProductsReviewResponse response, User user) {
        List<ProductReview> reviews = new ArrayList<>()

        if (response.products != null) {
            for (tools.dynamia.cms.products.dto.ProductDTO dto : (response.products)) {
                Product product = getProduct(site, dto)
                if (product != null) {
                    ProductReview review = getUserReview(product, user)

                    if (review == null) {
                        review = new ProductReview()
                        review.site = site
                        review.user = user
                        review.product = product
                        review.document = response.document

                        review.incomplete = true
                        if (getUserReview(product, user) == null) {
                            review = crudService().create(review)
                        }
                    }

                    if (review != null && review.incomplete) {
                        reviews.add(review)
                    }
                }

            }
        }
        return reviews
    }

}
