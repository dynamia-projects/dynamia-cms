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
package tools.dynamia.cms.site.core.listeners;

import tools.dynamia.cms.site.core.Aliasable;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Listener;

/**
 *
 * @author Mario Serrano
 * @since 1.2.0
 */
@Listener
public class AliasableCrudListener extends CrudServiceListenerAdapter<Aliasable> {

    @Override
    public void beforeCreate(Aliasable entity) {
        createAlias(entity);
    }

    @Override
    public void beforeUpdate(Aliasable entity) {
        createAlias(entity);
    }

    private void createAlias(Aliasable entity) {
        if (entity.getAlias() == null || entity.getAlias().isEmpty()) {
            if (entity.getAlias() == null || entity.getAlias().isEmpty()) {
                entity.setAlias(entity.aliasSource());
                if (entity.getAlias() != null && entity.getAlias().length() > 255) {
                    String alias = entity.getAlias().substring(0, 254);
                    entity.setAlias(alias);
                }
            }
            entity.setAlias(StringUtils.simplifiedString(entity.getAlias()));
        }
    }

}
