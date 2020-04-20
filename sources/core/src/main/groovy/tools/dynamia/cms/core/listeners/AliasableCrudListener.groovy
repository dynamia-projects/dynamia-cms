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
