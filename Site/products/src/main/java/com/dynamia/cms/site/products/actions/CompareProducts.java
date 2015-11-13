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
import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.products.ProductCompareGrid;
import com.dynamia.cms.site.products.ProductsUtil;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.services.CrudService;

/**
 *
 * @author mario_2
 */
@CMSAction
public class CompareProducts implements SiteAction {

	@Autowired
	private ProductsService service;
	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "compareProducts";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String[] idsString = (String[]) evt.getData();
		List<Long> ids = new ArrayList<>();
		for (String id : idsString) {
			ids.add(new Long(id));
		}

		List<Product> products = service.getProductsById(ids);
		ProductCompareGrid grid = new ProductCompareGrid(service, products);
		grid.loadData();

		ModelAndView mv = evt.getModelAndView();
		mv.addObject("comparegrid", grid);
		mv.addObject("title", buildTitle(products, evt.getSite()));
		ProductsUtil.setupDefaultVars(evt.getSite(), mv);
	}

	private String buildTitle(List<Product> products, Site site) {
		CMSUtil utils = new CMSUtil(site);
		List<String> names = new ArrayList<>();
		for (Product product : products) {
			names.add(utils.cropText(product.getName(), 30).replace(".", "").trim().toUpperCase());
		}
		return StringUtils.arrayToDelimitedString(names.toArray(), " vs ");
	}

}
