/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products;

import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.domain.ProductBrand;
import com.dynamia.cms.site.products.domain.ProductCategory;
import com.dynamia.cms.site.products.domain.ProductsSiteConfig;
import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.products.domain.Store;

/**
 *
 * @author mario
 */
@CMSModule
public class ProductsAdminModule implements AdminModule {

    @Override
    public String getGroup() {
        return "Products";
    }

    @Override
    public String getName() {
        return "Products";
    }

    @Override
    public AdminModuleOption[] getOptions() {
        return new AdminModuleOption[]{
            new AdminModuleOption("config", "Configuration", ProductsSiteConfig.class),
            new AdminModuleOption("products", "Products", Product.class),
            new AdminModuleOption("categories", "Categories", ProductCategory.class),
            new AdminModuleOption("brands", "Brands", ProductBrand.class),
            new AdminModuleOption("stores", "Stores", Store.class)};

    }

}
