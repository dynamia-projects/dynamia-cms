/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.products.admin.controllers

import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.zk.crud.TreeCrudController

/**
 * Created by Mario on 18/11/2014.
 */
class ProductCategoryTreeCrudController extends TreeCrudController<ProductCategory> {


    @Override
    protected Collection<ProductCategory> loadRoots() {
        return crudService.find(ProductCategory, QueryParameters.with("active", true).add("parent", QueryConditions.isNull()).orderBy("name"))
    }

    @Override
    protected Collection<ProductCategory> loadChildren(ProductCategory parent) {
        return crudService.find(ProductCategory, QueryParameters.with("active", true).add("parent", parent).orderBy("name"))
    }
}
