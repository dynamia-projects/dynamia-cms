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
package com.dynamia.cms.site.products.modules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;

import tools.dynamia.commons.collect.PagedList;
import tools.dynamia.domain.query.BooleanOp;
import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;

/**
 * Created by Mario on 18/11/2014.
 */
@CMSModule
public class ProductsCarouselModule extends AbstractModule {

	private static final String PARAM_PAGE_SIZE = "pageSize";

	private static final String PARAM_ORDER_BY = "orderBy";

	private static final String PARAM_STATUS = "status";

	private static final String PARAM_CATEGORY = "category";

	private static final String PARAM_COLUMNS = "columns";

	private static final String PARAM_TYPE = "type";

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	public ProductsCarouselModule() {
		super("products_carousel", "Products Carousel", "products/modules/carousel");
		setDescription("Show a products list carousel");
		putMetadata("author", "Mario Serrano Leones");
		putMetadata("version", "1.0");
		putMetadata("created at", "18-11-2014");
		putMetadata(PARAM_COLUMNS, "4");
		putMetadata(PARAM_TYPE, "featured");
		putMetadata(PARAM_PAGE_SIZE, "16");

	}

	@Override
	public void init(ModuleContext context) {
		ProductCarouselType type = ProductCarouselType.valueOf(getParameter(context, PARAM_TYPE).toUpperCase());
		int columns;
		try {
			columns = Integer.parseInt(getParameter(context, PARAM_COLUMNS));
		} catch (NumberFormatException e1) {
			columns = Integer.parseInt((String) getMetadata().get(PARAM_COLUMNS));
		}

		String category = getParameter(context, PARAM_CATEGORY);
		String status = getParameter(context, PARAM_STATUS);
		String orderBy = null;
		try {
			ProductOrderField orderField = ProductOrderField.valueOf(getParameter(context, PARAM_ORDER_BY).toUpperCase());
			orderBy = orderField.getField();
		} catch (Exception e) {
			// TODO: handle exception
		}

		String pageSizeParam = getParameter(context, PARAM_PAGE_SIZE);

		List<Product> products = new ArrayList<>();
		QueryParameters params = QueryParameters.with("site", context.getSite())
				.add("active", true)
				.paginate(columns * 4);

		if (pageSizeParam != null) {
			try {
				int pageSize = Integer.parseInt(pageSizeParam);
				params.paginate(pageSize);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		switch (type) {
		case ALL:
			products = service.getSpecialProducts(context.getSite());
			break;
		case FEATURED:
			params.add("featured", true);
			break;
		case NEW:
			params.add("newproduct", true);
			break;
		case SALE:
			params.add("sale", true);
			break;
		case MOST_VIEWED:
			params.orderBy("views", false);
			break;
		case CUSTOM:
			params.add(PARAM_STATUS, status);
			break;
		}

		if (orderBy != null) {
			params.orderBy(orderBy, true);
		}

		if (category != null && !category.isEmpty()) {
			try {
				Long categoryId = new Long(category);
				QueryParameters catParams = QueryParameters.with("category.id", QueryConditions.eq(categoryId, BooleanOp.OR))
						.add("category.parent.id", QueryConditions.eq(categoryId, BooleanOp.OR));
				params.addGroup(catParams, BooleanOp.AND);
			} catch (NumberFormatException nf) {

			}
		}

		if (products.isEmpty()) {
			products = crudService.find(Product.class, params);
		}

		if (products instanceof PagedList) {
			PagedList<Product> pagedList = (PagedList<Product>) products;
			products = pagedList.getDataSource().getPageData();
		}

		ModuleInstance mod = context.getModuleInstance();
		mod.addObject("products", products);
		mod.addObject(PARAM_COLUMNS, columns);
	}

	private String getParameter(ModuleContext context, String name) {
		String param = context.getModuleInstance().getParameterValue(name);
		if (param == null || param.trim().isEmpty()) {
			Object metadata = getMetadata().get(name);
			if (metadata != null) {
				param = metadata.toString();
			}
		}

		if (param != null) {
			param = param.trim();
		}

		return param;
	}

}
