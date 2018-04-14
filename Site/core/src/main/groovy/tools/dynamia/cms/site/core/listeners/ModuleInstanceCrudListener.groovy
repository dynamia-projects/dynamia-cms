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
package tools.dynamia.cms.site.core.listeners

import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Component

@Component
class ModuleInstanceCrudListener extends CrudServiceListenerAdapter<ModuleInstance> {

	@Override
    void beforeCreate(ModuleInstance entity) {
		checkAlias(entity)
    }

	@Override
    void beforeUpdate(ModuleInstance entity) {
		checkAlias(entity)
    }

	private void checkAlias(ModuleInstance entity) {
		if (entity.getAlias() == null || entity.getAlias().isEmpty()) {
			entity.setAlias(StringUtils.simplifiedString(entity.getTitle()))
        }

	}

}
