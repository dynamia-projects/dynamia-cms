/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.pages.PageNotFoundException;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductStock;
import com.dynamia.cms.site.products.domain.ProductUserStory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.cms.site.users.UserHolder;

import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;

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

		Product product = null;
		Long id = (Long) evt.getData();
		QueryParameters qp = QueryParameters.with("active", true).add("site", evt.getSite());

		if (id != null) {
			qp.add("id", id);
		} else if (evt.getRequest().getParameter("sku") != null) {
			String sku = evt.getRequest().getParameter("sku");
			qp.add("sku", sku);

		}
		product = crudService.findSingle(Product.class, qp);
		
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
