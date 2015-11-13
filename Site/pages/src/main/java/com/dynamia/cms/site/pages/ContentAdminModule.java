/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.domain.ContentAuthor;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageCategory;

/**
 *
 * @author mario
 */
@CMSModule
public class ContentAdminModule implements AdminModule {

	@Override
	public String getGroup() {
		return "Content";
	}

	@Override
	public String getName() {
		return "Site Content";
	}

	@Override
	public AdminModuleOption[] getOptions() {
		return new AdminModuleOption[] {
				new AdminModuleOption("authors", "Authors", ContentAuthor.class),
				new AdminModuleOption("categories", "Categories", PageCategory.class),
				new AdminModuleOption("pages", "Pages", Page.class, true, true, "edit", true)
		};
	}

}
