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