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
package tools.dynamia.cms.core.listeners

import tools.dynamia.cms.core.Aliasable
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

/**
 *
 * @author Mario Serrano
 * @since 1.2.0
 */
@Listener
class AliasableCrudListener extends CrudServiceListenerAdapter<Aliasable> {

    @Override
    void beforeCreate(Aliasable entity) {
        createAlias(entity)
    }

    @Override
    void beforeUpdate(Aliasable entity) {
        createAlias(entity)
    }

    private void createAlias(Aliasable entity) {
        if (entity.alias == null || entity.alias.empty) {
            if (entity.alias == null || entity.alias.empty) {
                entity.alias = entity.aliasSource()
                if (entity.alias != null && entity.alias.length() > 255) {
                    String alias = entity.alias.substring(0, 254)
                    entity.alias = alias
                }
            }
            entity.alias = StringUtils.simplifiedString(entity.alias)
        }
    }

}
