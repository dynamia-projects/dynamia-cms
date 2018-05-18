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
package tools.dynamia.cms.products.listeners

import tools.dynamia.cms.core.api.CMSListener
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.util.CrudServiceListenerAdapter

/**
 *
 * @author Mario Serrano Leones
 */
@CMSListener
class CategoryCrudServiceListener extends CrudServiceListenerAdapter<ProductCategory> {

    @Override
    void beforeCreate(ProductCategory entity) {
        generateAlias(entity)
    }

    @Override
    void beforeUpdate(ProductCategory entity) {
        generateAlias(entity)
    }

    private void generateAlias(ProductCategory entity) {
        String name = entity.name

        name = StringUtils.simplifiedString(name)
        entity.alias = name
    }

}
