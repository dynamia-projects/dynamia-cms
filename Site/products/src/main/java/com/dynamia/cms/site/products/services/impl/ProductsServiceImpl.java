/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.services.impl;

import static com.dynamia.tools.domain.query.QueryConditions.between;
import static com.dynamia.tools.domain.query.QueryConditions.geqt;
import static com.dynamia.tools.domain.query.QueryConditions.gt;
import static com.dynamia.tools.domain.query.QueryConditions.leqt;

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

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.mail.MailMessage;
import com.dynamia.cms.site.mail.services.MailService;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductShareForm;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductDetail;
import com.dynamia.cms.site.products.domain.ProductUserStory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.domain.Store;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.commons.collect.PagedList;
import com.dynamia.tools.domain.query.QueryConditions;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.util.QueryBuilder;
import com.dynamia.tools.integration.Containers;

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
	@Cacheable(value = "products", key = "'cat'+#site.key+#alias")
	public ProductCategory getCategoryByAlias(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("site", site);
		qp.add("active", true);
		qp.add("alias", QueryConditions.eq(alias));

		return crudService.findSingle(ProductCategory.class, qp);
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
	public List<ProductCategory> getCategories(ProductBrand brand) {
		String sql = QueryBuilder.select(ProductCategory.class, "pc").where("pc.site=:site")
				.and("pc.id in (select p.category.parent.id from Product p where p.site = :site and p.brand = :brand  and p.active=true)")
				.orderBy("pc.name").toString();

		Query query = entityManager.createQuery(sql);
		query.setParameter("site", brand.getSite());
		query.setParameter("brand", brand);

		return query.getResultList();
	}

	@Override
	public List<ProductCategory> getSubcategories(ProductCategory category, ProductBrand brand) {
		String sql = QueryBuilder
				.select(ProductCategory.class, "pc")
				.where("pc.site=:site")
				.and("pc.id in (select p.category.id from Product p where p.site = :site and p.brand = :brand and p.category.parent = :category and p.active=true)")
				.orderBy("pc.name").toString();

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
		qp.orderBy(cat + ".name, brand.name, price", true);

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
		if (params.size() > 0) {
			params.paginate(getDefaultPageSize(site));
			return crudService.executeQuery(builder, params);
		} else {
			return null;
		}

	}

	@Override
	@Cacheable(value = "products", key = "'fea'+#site.key")
	public List<Product> getFeaturedProducts(Site site) {
		QueryParameters qp = QueryParameters.with("active", true);
		qp.add("featured", true);
		qp.paginate(getDefaultPageSize(site) + 2);
		qp.orderBy("brand.name, price", true);
		PagedList<Product> list = (PagedList<Product>) crudService.find(Product.class, qp);
		return list.getDataSource().getPageData();
	}

	@Override
	@Cacheable(value = "products", key = "'sale'+#site.key")
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
	@Cacheable(value = "products", key = "#site.key")
	public List<ProductBrand> getBrands(Site site) {
		String sql = QueryBuilder.select(ProductBrand.class, "pb").where("pb.site=:site")
				.and("pb.id in (select p.brand.id from Product p where p.site = :site  and p.active=true)").orderBy("pb.name").toString();

		Query query = entityManager.createQuery(sql);
		query.setParameter("site", site);

		return query.getResultList();
	}

	@Override
	public List<ProductBrand> getBrands(ProductCategory category) {
		String sql = QueryBuilder
				.select(ProductBrand.class, "pb")
				.where("pb.site=:site")
				.and("pb.id in (select p.brand.id from Product p where p.site = :site and (p.category = :category or p.category.parent = :category  and p.active=true))")
				.orderBy("pb.name").toString();

		Query query = entityManager.createQuery(sql);
		query.setParameter("site", category.getSite());
		query.setParameter("category", category);

		return query.getResultList();
	}

	@Override
	@Cacheable(value = "products", key = "'brd'+#site.key+#alias")
	public ProductBrand getBrandByAlias(Site site, String alias) {
		QueryParameters qp = QueryParameters.with("site", site);
		qp.add("alias", QueryConditions.eq(alias));

		return crudService.findSingle(ProductBrand.class, qp);
	}

	@Override
	@Cacheable(value = "products", key = "'cfg'+#site.key")
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
						+ "or p.description like :param or p.sku like :param )").orderBy("p.brand.name, p.price");

		QueryParameters qp = new QueryParameters();
		qp.add("param", param);
		qp.add("site", site);
		qp.paginate(getDefaultPageSize(site));

		return crudService.executeQuery(query, qp);
	}

	@Override
	@Cacheable(value = "products", key = "'rld'+#product.id")
	public List<Product> getRelatedProducts(Product product) {
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

		qb.orderBy("p.price asc");
		String sql = qb.toString();
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(getDefaultPageSize(product.getSite()));
		qp.applyTo(query);
		return query.getResultList();
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
	@Cacheable(value = "products", key = "'specialCat'+#category.id")
	public List<Product> getSpecialProducts(ProductCategory category) {
		QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
				.and("(p.sale=true or p.featured=true or p.newproduct=true)")
				.and("(p.category = :category or p.category.parent=:category)").orderBy("p.price desc");

		QueryParameters qp = new QueryParameters();
		qp.add("category", category);
		qp.paginate(getDefaultPageSize(category.getSite()));

		return crudService.executeQuery(query, qp);

	}

	@Override
	@Cacheable(value = "products", key = "'priceVariations'+#site.key")
	public List<Product> getPriceVariationsProducts(Site site) {
		QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true").and("p.site = :site")
				.and("p.sale=false and p.featured=false and p.newproduct=false").and("p.price < p.lastPrice").orderBy("p.price desc");

		QueryParameters qp = new QueryParameters();
		qp.add("site", site);
		qp.paginate(getDefaultPageSize(site) * 2);

		PagedList list = (PagedList) crudService.executeQuery(query, qp);
		List<Product> products = list.getDataSource().getPageData();
		for (Product product : products) {
			product.setShowLastPrice(true);
		}
		return products;
	}

	@Override
	@Cacheable(value = "products", key = "'special'+#site.key")
	public List<Product> getSpecialProducts(Site site) {
		QueryBuilder query = QueryBuilder.select(Product.class, "p").where("p.active=true")
				.and("(p.sale=true or p.featured=true or p.newproduct=true)").and("p.site = :site").orderBy("p.price desc");

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

}
