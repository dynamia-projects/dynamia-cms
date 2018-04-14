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
package tools.dynamia.cms.site.products

import tools.dynamia.cms.site.core.api.AdminModule
import tools.dynamia.cms.site.core.api.AdminModuleOption
import tools.dynamia.cms.site.core.api.CMSModule
import tools.dynamia.cms.site.products.domain.*

/**
 * @author Mario Serrano Leones
 */
@CMSModule
class ProductsAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "Products"
    }

    @Override
    String getName() {
        return "Store"
    }

    @Override
    String getImage() {
        return "fa-home"
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("config", "Configuration", ProductsSiteConfig.class, false, true),
                new AdminModuleOption("products", "Products", Product.class, true, true, "th", true),
                new AdminModuleOption("categories", "Categories", ProductCategory.class),
                new AdminModuleOption("brands", "Brands", ProductBrand.class),
                new AdminModuleOption("stores", "Branches", Store.class),
                new AdminModuleOption("templates", "Templates", ProductTemplate.class),
                new AdminModuleOption("reviews", "Reviews", ProductReview.class)
        ]

    }

}
