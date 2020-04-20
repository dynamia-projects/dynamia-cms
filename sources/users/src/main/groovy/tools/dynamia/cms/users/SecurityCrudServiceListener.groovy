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
package tools.dynamia.cms.users

import tools.dynamia.domain.BaseEntity
import tools.dynamia.domain.util.CrudServiceListenerAdapter

class SecurityCrudServiceListener extends CrudServiceListenerAdapter<Object> {

    @Override
    void beforeCreate(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity ent = (BaseEntity) entity
            if (ent.creator == null || ent.creator.empty) {
                ent.creator = UserHolder.get().fullName
            }
        }
    }

    @Override
    void beforeUpdate(Object entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity ent = (BaseEntity) entity
            ent.lastUpdater = UserHolder.get().fullName
        }
    }

    @Override
    void beforeDelete(Object entity) {
        // TODO Auto-generated method stub
    }

}
