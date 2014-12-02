/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.ext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.menus.MenuContext;
import com.dynamia.cms.site.menus.api.MenuItemTypeExtension;
import com.dynamia.cms.site.menus.domain.MenuItem;
import com.dynamia.cms.site.menus.domain.MenuItemGroup;
import com.dynamia.cms.site.menus.domain.MenuItemParameter;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.domain.services.CrudService;

/**
 *
 * @author mario
 */
@CMSExtension
public class SubcategoriesMenuItemType implements MenuItemTypeExtension {

	@Autowired
	private ProductsService service;

	@Autowired
	private CrudService crudService;

	@Override
	public String getId() {
		return "subcategories";
	}

	@Override
	public String getName() {
		return "Sub categories menu item";
	}

	@Override
	public String getDescription() {
		return "A sub category list items, you should specified a category id using item parameters";
	}

	@Override
	public void setupMenuItem(MenuContext context) {
		MenuItemParameter categoryParam = context.getParameter("category");
		if (categoryParam != null) {
			Long categoryId = new Long(categoryParam.getValue());
			ProductCategory category = crudService.find(ProductCategory.class, categoryId);
			if (category != null) {
				List<ProductCategory> subcategories = service.getSubcategories(category);
				MenuItem item = context.getMenuItem();
				if (item.getHref() == null) {
					item.setHref("/store/categories/" + category.getId() + "/" + category.getAlias());
				}

				item.getSubitems().clear();
				if (subcategories != null) {
					for (ProductCategory subcat : subcategories) {
						item.addMenuItem(new MenuItem(clean(getCategoryName(subcat)), "/store/categories/" + subcat.getId() + "/"
								+ subcat.getAlias()));
					}
				}

				item.getItemsGroups().clear();
				List<ProductCategory> relatedCategories = service.getRelatedCategories(category);
				if (relatedCategories != null && !relatedCategories.isEmpty()) {
					for (ProductCategory relatedCategory : relatedCategories) {
						String groupName = getCategoryName(relatedCategory);
						
						MenuItemGroup group = new MenuItemGroup(groupName);
						item.addMenuItemGroup(group);
						List<ProductCategory> relatedSubcategories = service.getSubcategories(relatedCategory);
						if (relatedSubcategories != null) {
							for (ProductCategory relSubcat : relatedSubcategories) {
								group.addMenuItem(new MenuItem(clean(relSubcat.getName()), "/store/categories/" + relSubcat.getId() + "/"
										+ relSubcat.getAlias()));
							}
						}
					}
				}
			}
		}

	}

	private String getCategoryName(ProductCategory category) {
		String name = category.getName();
		if(category.getAlternateName()!=null && !category.getAlternateName().trim().isEmpty()){
			name = category.getAlternateName();
		}
		return name;
	}

	private String clean(String name) {
		name = name.replace("-", " ");
		name = name.trim();

		return name;
	}

}
