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
package tools.dynamia.cms.core.services.impl

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import tools.dynamia.cms.core.api.Module
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.domain.util.QueryBuilder
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Service

@Service
@CompileStatic
class ModulesServiceImpl implements ModulesService {

	private static final String CACHE_NAME = "modules"
	@Autowired
	private CrudService crudService

	@Override
	List<Module> getInstalledModules() {
		List<Module> modules = new ArrayList<>()
		modules.addAll(Containers.get().findObjects(Module.class))
		return modules
	}

	@Override
	void initModuleInstance(ModuleInstance moduleInstance) {
		Module module = getModule(moduleInstance)
		if (module != null) {
			ModuleContext context = new ModuleContext(moduleInstance)
			module.init(context)
		}
	}

	@Override
	@Cacheable(value = ModulesServiceImpl.CACHE_NAME, key = "#site.key+#position")
	List<ModuleInstance> getModules(Site site, String position) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true).add("position",
				QueryConditions.eq(position))
		List<ModuleInstance> instances = crudService.find(ModuleInstance.class, params)
		for (ModuleInstance moduleInstance : instances) {
			moduleInstance.parameters.size()
			initModuleInstance(moduleInstance)
		}
		return instances

	}

	@Override
	@Cacheable(value = ModulesServiceImpl.CACHE_NAME, key = "'positions'+#site.key")
	List<String> getUsedPositions(Site site) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true)
		QueryBuilder queryBuilder = QueryBuilder.select(ModuleInstance.class, "m", "position").where(params)
				.groupBy("position")
		return crudService.executeQuery(queryBuilder, params)
	}

	@Override
	List<String> getAllUsedPositions(Site site) {
		QueryParameters params = QueryParameters.with("site", site)
		QueryBuilder queryBuilder = QueryBuilder.select(ModuleInstance.class, "m", "position").where(params)
				.groupBy("position")
		return crudService.executeQuery(queryBuilder, params)
	}

	@Override
	Module getModule(ModuleInstance instance) {
		if (instance != null && instance.moduleId != null) {
			for (Module module : Containers.get().findObjects(Module.class)) {
				if (instance.moduleId.equals(module.id)) {
					return module
				}
			}
		}
		return null
	}

}
