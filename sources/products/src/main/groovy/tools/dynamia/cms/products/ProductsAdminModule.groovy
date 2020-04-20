/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.products

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductBrand
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.domain.ProductReview
import tools.dynamia.cms.products.domain.ProductTemplate
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.domain.Store

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
