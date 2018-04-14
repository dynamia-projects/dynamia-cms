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
package tools.dynamia.cms.site.products.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.remoting.RemoteConnectFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.site.core.DynamiaCMS
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.mail.MailMessage
import tools.dynamia.cms.site.mail.services.MailService
import tools.dynamia.cms.site.products.ProductSearchForm
import tools.dynamia.cms.site.products.ProductShareForm
import tools.dynamia.cms.site.products.api.ProductReviewsConnector
import tools.dynamia.cms.site.products.domain.*
import tools.dynamia.cms.site.products.dto.ProductDTO
import tools.dynamia.cms.site.products.dto.ProductsReviewResponse
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
import tools.dynamia.commons.StringUtils
import tools.dynamia.commons.collect.PagedList
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.query.BooleanOp
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.domain.util.QueryBuilder
import tools.dynamia.integration.Containers
import tools.dynamia.web.util.HttpRemotingServiceClient

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.stream.Collectors

import static tools.dynamia.domain.query.QueryConditions.*

/**
 * @author Mario Serrano Leones
 */
@Service
class ProductsServiceImpl implements ProductsService {

    private static final String CACHE_NAME = "products"

    @Autowired
    private CrudService crudService

    @PersistenceContext
    private EntityManager entityManager

    @Autowired
    private MailService mailService

    @Autowired
    private UserService userService

    @Override
    void generateToken(ProductsSiteConfig config) {
        config.setToken(StringUtils.randomString())
    }

    @Override
    ProductsSiteConfig getSiteConfig(String token) {
        return crudService.findSingle(ProductsSiteConfig.class, "token", token)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#site.key+#alias")
    ProductCategory getCategoryByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("active", true)
        qp.add("alias", QueryConditions.eq(alias))

        return crudService.findSingle(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#categoryId")
    ProductCategory getCategoryById(Long categoryId) {
        return crudService.find(ProductCategory.class, categoryId)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cat'+#site.key")
    List<ProductCategory> getCategories(Site site) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("parent", QueryConditions.isNull())
        qp.add("active", true)
        qp.orderBy("order", true)

        return crudService.find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'subcatsOf'+#category.id")
    List<ProductCategory> getSubcategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("site", category.getSite())
        qp.add("parent", category)
        qp.add("active", true)
        qp.orderBy("order", true)

        return crudService.find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandCat'+#brand.id")
    List<ProductCategory> getCategories(ProductBrand brand) {
        String sql = QueryBuilder.select(ProductCategory.class, "pc").where("pc.site=:site").and(
                "pc.id in (select p.category.parent.id from Product p where p.site = :site and p.brand = :brand  and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", brand.getSite())
        query.setParameter("brand", brand)

        return query.getResultList()
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandCat'+#brand.id+#category.id")
    List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand) {
        String sql = QueryBuilder.select(ProductCategory.class, "pc").where("pc.site=:site").and(
                "pc.id in (select p.category.id from Product p where p.site = :site and p.brand = :brand and p.category.parent = :category and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", brand.getSite())
        query.setParameter("brand", brand)
        query.setParameter("category", category)

        return query.getResultList()
    }

    @Override
    List<Product> getProducts(ProductCategory category) {
        return getProducts(category, "price", true)
    }

    @Override
    List<Product> getProducts(ProductCategory category, String orderfield, boolean asc) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", category.getSite())

        if (category.getParent() == null) {
            qp.addGroup(QueryParameters.with("category.parent", QueryConditions.eq(category, BooleanOp.OR))
                    .add("category", QueryConditions.eq(category, BooleanOp.OR)), BooleanOp.AND)
        } else {
            qp.add("category", category)
        }

        qp.paginate(getDefaultPageSize(category.getSite()))
        qp.orderBy(orderfield, asc)

        return crudService.find(Product.class, qp)
    }

    @Override
    List<Product> getProducts(ProductBrand brand) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("brand", brand)
        qp.orderBy("price", true)
        qp.paginate(getDefaultPageSize(brand.getSite()))
        return crudService.find(Product.class, qp)
    }

    @Override
    List<Product> filterProducts(Site site, QueryParameters params) {
        params.add("site", site)

        return crudService.find(Product.class, params)
    }

    @Override
    List<Product> filterProducts(Site site, ProductSearchForm form) {

        QueryParameters params = new QueryParameters()
        params.add("active", true)

        if (form.getName() != null && !form.getName().trim().isEmpty()) {
            params.add("name", form.getName())
        }

        if (form.getMaxPrice() != null && form.getMinPrice() == null) {
            params.add("price", leqt(form.getMaxPrice()))
        }

        if (form.getMaxPrice() == null && form.getMinPrice() != null) {
            params.add("price", geqt(form.getMinPrice()))
        }

        if (form.getMaxPrice() != null && form.getMinPrice() != null) {
            params.add("price", between(form.getMinPrice(), form.getMaxPrice()))
        }

        if (form.isStock()) {
            params.add("stock", gt(0))
        }

        if (form.getOrder() != null) {
            params.orderBy(form.getOrder().getField(), form.getOrder().isAsc())
        } else {
            params.orderBy("price", true)
        }

        QueryBuilder builder = QueryBuilder.fromParameters(Product.class, "p", params)
        if (form.getBrandId() != null) {
            builder.and("p.brand.id = :brandId")
            params.add("brandId", form.getBrandId())
        }
        if (form.getCategoryId() != null) {
            builder.and("(p.category.id = :category or p.category.parent.id = :category)")
            params.add("category", form.getCategoryId())
        }

        Map<String, String> map = new HashMap<>()
        filterByDetail(form.getDetail(), "1", params, builder, map)
        filterByDetail(form.getDetail2(), "2", params, builder, map)
        filterByDetail(form.getDetail3(), "3", params, builder, map)
        filterByDetail(form.getDetail4(), "4", params, builder, map)

        form.setAttribute("filteredDetails", map)


        if (params.size() > 0) {
            params.paginate(getDefaultPageSize(site))
            return crudService.executeQuery(builder, params)
        } else {
            return null
        }

    }

    private String filterByDetail(String det, String suffix, QueryParameters params, QueryBuilder builder, Map<String, String> map) {
        try {
            if (det != null && !det.isEmpty() && det.contains(";")) {
                String detail[] = det.split(";")
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
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp)
        return list.getDataSource().getPageData()
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'sale'+#site.key")
    List<Product> getSaleProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("sale", true)
        qp.paginate(getDefaultPageSize(site) + 2)
        qp.orderBy("brand.name, price", true)
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp)
        return list.getDataSource().getPageData()
    }

    @Override
    List<Product> getMostViewedProducts(Site site) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site)
        qp.paginate(getDefaultPageSize(site) + 2)
        qp.orderBy("views", false)
        PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp)
        return list.getDataSource().getPageData()
    }

    @Override
    Product getProductBySku(Site site, String sku) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("sku", sku)

        return crudService.findSingle(Product.class, qp)

    }

    @Override
    Product getProductById(Site site, Long id) {
        QueryParameters qp = QueryParameters.with("active", true).add("site", site).add("id", id)

        return crudService.findSingle(Product.class, qp)

    }

    @Override
    List<Product> getProductsById(List<Long> ids) {
        QueryParameters qp = QueryParameters.with("active", true)
        qp.add("id", QueryConditions.in(ids))
        return crudService.find(Product.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "#site.key")
    List<ProductBrand> getBrands(Site site) {
        String sql = QueryBuilder.select(ProductBrand.class, "pb").where("pb.site=:site")
                .and("pb.id in (select p.brand.id from Product p where p.site = :site  and p.active=true)")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", site)

        return query.getResultList()
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brandByCat'+#category.id")
    List<ProductBrand> getBrands(ProductCategory category) {
        String sql = QueryBuilder.select(ProductBrand.class, "pb").where("pb.site=:site").and(
                "pb.id in (select p.brand.id from Product p where p.site = :site and ((p.category = :category or p.category.parent = :category)  and p.active=true and p.stock > 0))")
                .orderBy("name").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", category.getSite())
        query.setParameter("category", category)

        return query.getResultList()
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'brand'+#site.key+#alias")
    ProductBrand getBrandByAlias(Site site, String alias) {
        QueryParameters qp = QueryParameters.with("site", site)
        qp.add("alias", QueryConditions.eq(alias))

        return crudService.findSingle(ProductBrand.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'cfg'+#site.key")
    ProductsSiteConfig getSiteConfig(Site site) {
        return crudService.findSingle(ProductsSiteConfig.class, "site", site)
    }

    @Override
    List<Product> find(Site site, String param) {
        if (param == null || param.isEmpty()) {
            return Collections.EMPTY_LIST
        }

        QueryBuilder query = QueryBuilder.select(Product.class, "p")
                .leftJoin("p.brand brd").where("p.active = true").and("p.site = :site")
                .and("(p.name like :param or p.category.name like :param or brd.name like :param "
                        + "or p.description like :param or p.sku like :param )")
                .orderBy("p.price")

        QueryParameters qp = new QueryParameters()
        qp.add("param", param)
        qp.add("site", site)
        qp.paginate(getDefaultPageSize(site))

        return crudService.executeQuery(query, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'rld'+#product.id")
    List<Product> getRelatedCategoryProducts(Product product) {
        QueryParameters qp = new QueryParameters()
        QueryBuilder qb = QueryBuilder.select(Product.class, "p").where("p.active = true")

        qp.add("site", product.getSite())
        qp.add("category", product.getCategory())
        qb.and("p.site = :site")

        if (product.getCategory().getParent() != null) {
            qb.and("(p.category.parent = :parentCategory or p.category = :category)")
            qp.add("parentCategory", product.getCategory().getParent())
        } else {
            qb.and("p.category = :category")
        }

        qb.orderBy("price asc")
        String sql = qb.toString()
        Query query = entityManager.createQuery(sql)
        query.setMaxResults(getDefaultPageSize(product.getSite()))
        qp.applyTo(query)
        return query.getResultList()
    }

    /**
     * @param product
     * @param requires
     * @return
     */
    @Override
    List<RelatedProduct> getRelatedProducts(Product product, boolean requires) {
        List<RelatedProduct> relateds = new ArrayList<>()

        relateds.addAll(crudService.find(RelatedProduct.class, QueryParameters.with("active", true)
                .add("targetProduct", product).add("required", requires).orderBy("price", false)))

        List<ProductCategory> categories = new ArrayList<>()

        if (product.getCategory() != null) {
            categories.add(product.getCategory())

            if (product.getCategory().getParent() != null) {
                categories.add(product.getCategory().getParent())
            }
        }
        if (!categories.isEmpty()) {
            relateds.addAll(crudService.find(RelatedProduct.class,
                    QueryParameters.with("active", true).add("targetCategory", QueryConditions.in(categories))
                            .add("required", requires).orderBy("price", false)))
        }

        return relateds
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateViewsCount(Product product) {
        crudService.updateField(product, "views", product.getViews() + 1L)
    }

    private int getDefaultPageSize(Site site) {
        ProductsService self = Containers.get().findObject(ProductsService.class)
        ProductsSiteConfig config = self.getSiteConfig(site)
        if (config != null) {
            return config.getProductsPerPage()
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
        qp.paginate(getDefaultPageSize(category.getSite()))

        return crudService.executeQuery(query, qp)

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

        PagedList list = (PagedList) crudService.executeQuery(query, qp)
        List<Product> products = list.getDataSource().getPageData()

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

        PagedList list = (PagedList) crudService.executeQuery(query, qp)
        return list.getDataSource().getPageData()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void updateProductStoryViews(Product product) {
        try {
            if (UserHolder.get().isAuthenticated()) {
                ProductUserStory story = getProductStory(product, UserHolder.get().getCurrent())
                if (story.getId() == null) {
                    story.setFirstView(new Date())
                }
                story.setLastView(new Date())
                story.setViews(story.getViews() + 1)
                crudService.save(story)
            }
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    void updateProductStoryShops(Product product) {
        try {
            if (UserHolder.get().isAuthenticated()) {
                ProductUserStory story = getProductStory(product, UserHolder.get().getCurrent())
                if (story.getId() == null) {
                    story.setFirstShop(new Date())
                }
                story.setLastShop(new Date())
                story.setShops(story.getShops() + 1)
                crudService.save(story)
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

        ProductUserStory userStory = crudService.findSingle(ProductUserStory.class, qp)
        if (userStory == null) {
            userStory = new ProductUserStory()
            userStory.setProduct(product)
            userStory.setUser(user)
        }

        return userStory
    }

    @Override
    List<Product> getRecentProducts(User user) {
        String sql = "select s.product from ProductUserStory s where s.user = :user order by s.lastView desc"
        Query query = entityManager.createQuery(sql)
        query.setMaxResults(getDefaultPageSize(user.getSite()))
        query.setParameter("user", user)

        return query.getResultList()

    }

    @Override
    List<ProductDetail> getProductsDetails(List<Product> products) {
        QueryParameters qp = QueryParameters.with("product", QueryConditions.in(products))
        return crudService.find(ProductDetail.class, qp)
    }

    @Override
    List<Store> getStores(Site site) {
        QueryParameters qp = QueryParameters.with("site", site).orderBy("contactInfo.city", true)

        return crudService.find(Store.class, qp)
    }

    @Override
    void shareProduct(ProductShareForm form) {
        MailMessage message = new MailMessage()
        Product product = getProductById(form.getSite(), form.getProductId())
        ProductsSiteConfig config = getSiteConfig(form.getSite())
        message.setTemplate(config.getShareProductMailTemplate())
        message.getTemplateModel().put("product", product)
        message.getTemplateModel().put("form", form)
        message.setTo(form.getFriendEmail())
        message.setMailAccount(config.getMailAccount())

        Path resources = DynamiaCMS.getSitesResourceLocation(product.getSite())
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage()).toFile()
            message.addAttachtment(image)
        }

        if (product.getImage2() != null && !product.getImage2().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage2()).toFile()
            message.addAttachtment(image)
        }

        if (product.getImage3() != null && !product.getImage3().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage3()).toFile()
            message.addAttachtment(image)
        }

        if (product.getImage4() != null && !product.getImage4().isEmpty()) {
            File image = resources.resolve("products/images/" + product.getImage4()).toFile()
            message.addAttachtment(image)
        }

        mailService.send(message)

    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'catRelated'+#category.id")
    List<ProductCategory> getRelatedCategories(ProductCategory category) {
        QueryParameters qp = QueryParameters.with("relatedCategory", category)
        qp.add("active", true)
        qp.add("site", category.getSite())
        qp.orderBy("order", true)
        return crudService.find(ProductCategory.class, qp)
    }

    @Override
    @Cacheable(value = ProductsServiceImpl.CACHE_NAME, key = "'catDetails'+#category.id")
    List<ProductCategoryDetail> getCategoryDetails(ProductCategory category) {
        String sql = QueryBuilder.select(ProductCategoryDetail.class, "pcd").where("pcd.site=:site")
                .and("pcd.filterable=true")
                .and("(pcd.category = :category or pcd.category.parent = :category) and pcd.name in (select pd.name from ProductDetail pd where pd.site = :site and (pd.product.category = :category or pd.product.category.parent = :category)  and pd.product.active=true)")
                .groupBy("name").orderBy("order").toString()

        Query query = entityManager.createQuery(sql)
        query.setParameter("site", category.getSite())
        query.setParameter("category", category)

        List<ProductCategoryDetail> details = query.getResultList()


        String sqlValues = "select det.name, det.value from ProductDetail det inner join det.product p inner join p.category c"
                + " where (c = :category or c.parent = :category)  and p.active=true group by det.value order by det.value"
        List values = entityManager.createQuery(sqlValues).setParameter("category", category).getResultList()

        for (ProductCategoryDetail det : details) {
            for (Object objet : values) {
                Object[] catValue = (Object[]) objet

                if (det.getName().trim().equals(catValue[0].toString().trim())) {
                    if (catValue[1] != null && !catValue[1].toString().isEmpty()) {
                        det.getCurrentValues().add(catValue[1].toString())
                    }
                }
            }
        }

        return details
    }

    @Override
    @Transactional
    int computeProductCountByCategory(Site site) {
        if (site != null) {
            String sql = "update ProductCategory pc set pc.productsCount = (select count(p) from Product p where p.active=true and p.site = :site and p.category.id = pc.id) where pc.site = :site "
            int result = entityManager.createQuery(sql).setParameter("site", site).executeUpdate()

            return result
        }
        return 0
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveReview(Product product, String comment, int rate) {

        User user = UserHolder.get().getCurrent()

        ProductReview rev = getUserReview(product, user)

        if (rev != null && !rev.isIncomplete()) {
            throw new ValidationError("Ya has enviado una reseña sobre este producto")
        } else if (rev == null) {
            rev = new ProductReview()
        }

        rev.setUser(user)
        rev.setProduct(product)
        rev.setSite(product.getSite())
        rev.setComment(comment)
        rev.setStars(rate)
        rev.setIncomplete(false)
        crudService.save(rev)
    }

    @Override
    ProductReview getUserReview(Product product, User user) {
        return crudService.findSingle(ProductReview.class, QueryParameters.with("product", product).add("user", user))
    }

    @Override
    List<ProductReview> getIncompleteProductReviews(User user) {
        List<ProductReview> reviews = crudService.find(ProductReview.class, QueryParameters.with("user", user).add("incomplete", true))

        return reviews
    }

    @Override
    @Transactional
    void computeProductStars(Product product) {
        product = crudService.find(Product.class, product.getId())

        Double stars = crudService.executeProjection(Double.class,
                "select avg(r.stars) from ProductReview r where r.product = :product and r.incomplete=false",
                QueryParameters.with("product", product))

        if (stars == null) {
            stars = 0.0
        }

        product.setReviews(crudService.count(ProductReview.class, QueryParameters.with("product", product)))
        product.setStars1Count(crudService.count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 1)))
        product.setStars2Count(crudService.count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 2)))
        product.setStars3Count(crudService.count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 3)))
        product.setStars4Count(crudService.count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 4)))
        product.setStars5Count(crudService.count(ProductReview.class,
                QueryParameters.with("product", product).add("incomplete", false).add("stars", 5)))

        if (stars != null && stars > 0) {
            product.setStars(stars)
        } else {
            product.setStars(0.0)
        }

        crudService.update(product)
    }

    @Override
    List<ProductReview> getTopReviews(Product product, int max) {
        List<ProductReview> result = crudService.find(ProductReview.class, QueryParameters.with("product", product)
                .add("incomplete", false).orderBy("creationDate", false).paginate(max))

        if (result instanceof PagedList) {
            result = ((PagedList) result).getDataSource().getPageData()
        }

        return result
    }

    @Override
    ProductsReviewResponse requestExternalReviews(ProductsSiteConfig config, String requestUuid) {
        if (config != null && config.getReviewsConnectorURL() != null && !config.getReviewsConnectorURL().isEmpty()) {
            ProductReviewsConnector connector = HttpRemotingServiceClient.build(ProductReviewsConnector.class)
                    .setServiceURL(config.getReviewsConnectorURL()).getProxy()

            if (connector != null) {
                try {
                    return connector.requestReviews(requestUuid)
                } catch (RemoteConnectFailureException e) {
                    return ProductsReviewResponse.rejected("No hay conexion con servicio")
                }
            }
        }

        return ProductsReviewResponse.rejected()
    }

    @Override
    User findUserForReview(Site site, ProductsReviewResponse response) {
        String email = response.getEmail()

        if (email.contains(",")) {
            try {
                email = email.split(",")[0]
                email = email.trim()
            } catch (Exception e) {

            }
        }

        User user = userService.getUser(site, email, response.getIdentification())
        if (user == null) {
            user = userService.getUser(site, email)
        }


        if (user == null) {
            user = new User()
            user.setSite(site)
            user.setUsername(email)
            user.setFirstName(response.getName())
            user.setLastName(response.getLastName())
            user.setEnabled(true)
            user.getContactInfo().setAddress(response.getAddress())
            user.getContactInfo().setCity(response.getCity())
            user.getContactInfo().setEmail(response.getEmail())
            user.getContactInfo().setCountry(response.getCountry())
            user.getContactInfo().setMobileNumber(response.getMobileNumber())
            user.getContactInfo().setPhoneNumber(response.getPhoneNumber())
            user.setIdentification(response.getIdentification())
            user.setExternalRef(response.getExternalRef())
            userService.setupPassword(user, response.getIdentification())
            user = crudService.create(user)
        }
        return user
    }

    @Override
    Product getProduct(Site site, ProductDTO dto) {
        Product product = null

        if (dto != null) {
            if (dto.getExternalRef() != null) {
                product = crudService.findSingle(Product.class, QueryParameters.with("site", site).add("active", true)
                        .add("externalRef", dto.getExternalRef()))
            }

            if (product == null && dto.getSku() != null && !dto.getSku().isEmpty()) {
                product = crudService.findSingle(Product.class, QueryParameters.with("site", site).add("active", true)
                        .add("sku", dto.getSku()).setAutocreateSearcheableStrings(false))
            }
        }

        return product
    }

    @Override
    List<ProductReview> getExternalProductReviews(Site site, ProductsReviewResponse response, User user) {
        List<ProductReview> reviews = new ArrayList<>()

        if (response.getProducts() != null) {
            for (ProductDTO dto : response.getProducts()) {
                Product product = getProduct(site, dto)
                if (product != null) {
                    ProductReview review = getUserReview(product, user)

                    if (review == null) {
                        review = new ProductReview()
                        review.setSite(site)
                        review.setUser(user)
                        review.setProduct(product)
                        review.setDocument(response.getDocument())

                        review.setIncomplete(true)
                        if (getUserReview(product, user) == null) {
                            review = crudService.create(review)
                        }
                    }

                    if (review != null && review.isIncomplete()) {
                        reviews.add(review)
                    }
                }

            }
        }
        return reviews
    }

}
