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
package tools.dynamia.cms.site.products;

import tools.dynamia.cms.site.core.api.AdminModule;
import tools.dynamia.cms.site.core.api.AdminModuleOption;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductBrand;
import tools.dynamia.cms.site.products.domain.ProductCategory;
import tools.dynamia.cms.site.products.domain.ProductTemplate;
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.domain.Store;

/**
 *
 * @author Mario Serrano Leones
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
            new AdminModuleOption("config", "Configuration", ProductsSiteConfig.class, false, true),
            new AdminModuleOption("products", "Products", Product.class, true, true, "th", true),
            new AdminModuleOption("categories", "Categories", ProductCategory.class),
            new AdminModuleOption("brands", "Brands", ProductBrand.class),
            new AdminModuleOption("stores", "Stores", Store.class),
            new AdminModuleOption("templates", "Templates", ProductTemplate.class)};

    }

}
