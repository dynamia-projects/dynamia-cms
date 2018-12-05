/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
