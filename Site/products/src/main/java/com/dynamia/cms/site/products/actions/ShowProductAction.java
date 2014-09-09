/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.pages.PageNotFoundException;
import com.dynamia.cms.site.products.ProductShareForm;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductStock;
import com.dynamia.cms.site.products.domain.ProductUserStory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.New;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowProductAction implements SiteAction {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "showProduct";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();

		Long id = (Long) evt.getData();

		QueryParameters qp = QueryParameters.with("id", id).add("active", true).add("site", evt.getSite());

		Product product = crudService.findSingle(Product.class, qp);
		if (product == null) {
			throw new PageNotFoundException("Product not found");
		}

		String price = "";
		try {
			CMSUtil util = new CMSUtil(evt.getSite());
			ProductsSiteConfig config = service.getSiteConfig(evt.getSite());
			price = " - " + util.formatNumber(product.getPrice(), config.getPricePattern());
		} catch (Exception e) {
		}

		service.updateViewsCount(product);
		service.updateProductStoryViews(product);
		ProductUserStory story = service.getProductStory(product, UserHolder.get().getCurrent());
		if (story != null) {
			mv.addObject("prd_story", story);
		}

		QueryParameters sdparams = QueryParameters.with("product", product).orderBy("store.contactInfo.city asc, store.name", true);
		List<ProductStock> stockDetails = crudService.find(ProductStock.class, sdparams);

		mv.addObject("prd_product", product);
		mv.addObject("prd_stock_details", stockDetails);
		mv.addObject("prd_relatedProducts", service.getRelatedProducts(product));
		mv.addObject("prd_config", service.getSiteConfig(evt.getSite()));
		mv.addObject("title", product.getName().toUpperCase() + price);
		mv.addObject("subtitle", product.getCategory().getName());
		mv.addObject("icon", "info-sign");

		mv.addObject("metaDescription", product.getDescription());
		if (product.getTags() != null && !product.getTags().isEmpty()) {
			mv.addObject("metaKeywords", product.getTags());
		}

		String baseImageUrl = SiteContext.get().getSiteURL() + "/resources/products/images/";
		List<String> pageImages = new ArrayList<>();
		pageImages.add(baseImageUrl + product.getImage());
		if (product.getImage2() != null) {
			pageImages.add(baseImageUrl + product.getImage2());
		}
		if (product.getImage3() != null) {
			pageImages.add(baseImageUrl + product.getImage3());
		}
		if (product.getImage4() != null) {
			pageImages.add(baseImageUrl + product.getImage4());
		}
		mv.addObject("pageImages", pageImages);

	}

}
