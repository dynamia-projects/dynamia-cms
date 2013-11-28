/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages;

import com.dynamia.cms.site.core.domain.ContentAuthor;
import com.dynamia.cms.site.core.ext.AdminModule;
import com.dynamia.cms.site.core.ext.AdminModuleOption;
import com.dynamia.cms.site.core.ext.InstallModule;
import com.dynamia.cms.site.pages.domain.Page;
import com.dynamia.cms.site.pages.domain.PageCategory;

/**
 *
 * @author mario
 */
@InstallModule
public class ContentAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "Content";
    }

    @Override
    public String getName() {
        return "Pages and Content";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("authors", "Authors", ContentAuthor.class),
            new AdminModuleOption("categories", "Categories", PageCategory.class),
            new AdminModuleOption("pages", "Pages", Page.class)
        };
    }

}
