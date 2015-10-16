/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.actions;

import com.dynamia.cms.site.core.actions.ActionEvent;
import com.dynamia.cms.site.core.actions.SiteAction;
import com.dynamia.cms.site.core.actions.SiteActionManager;
import com.dynamia.cms.site.core.api.CMSAction;
import com.dynamia.cms.site.products.ProductSearchForm;
import com.dynamia.cms.site.products.ProductSearchOrder;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSAction
public class ShowBrandAction implements SiteAction {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getName() {
		return "showProductBrand";
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ModelAndView mv = evt.getModelAndView();
		ProductSearchForm form = (ProductSearchForm) evt.getData();

		ProductBrand brand = crudService.find(ProductBrand.class, form.getBrandId());

		mv.addObject("prd_brand", brand);
		form.setOrder(ProductSearchOrder.MINPRICE);

		if (!evt.getRequest().getParameterMap().isEmpty()) {
			form.setDetail(evt.getRequest().getParameter("q"));
		}

		SiteActionManager.performAction("searchProducts", mv, evt.getRequest(), form);
		ProductCategory category = (ProductCategory) mv.getModel().get("prd_category");

		if (category != null) {
			mv.addObject("title", category.getName());
			mv.addObject("prd_subcategories", service.getSubcategories(category, brand));
			mv.addObject("prd_category_details", service.getCategoryDetails(category));
		} else {
			mv.addObject("title", brand.getName());
			mv.addObject("prd_subcategories", service.getCategories(brand));
		}

	}

}
