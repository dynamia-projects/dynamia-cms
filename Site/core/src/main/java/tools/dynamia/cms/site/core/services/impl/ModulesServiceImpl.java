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
package tools.dynamia.cms.site.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import tools.dynamia.cms.site.core.api.Module;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.cms.site.core.domain.Site;

import tools.dynamia.domain.query.QueryConditions;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.QueryBuilder;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.sterotypes.Service;

@Service
public class ModulesServiceImpl implements ModulesService {

	private static final String CACHE_NAME = "modules";
	@Autowired
	private CrudService crudService;

	@Override
	public List<Module> getInstalledModules() {
		List<Module> modules = new ArrayList<>();
		modules.addAll(Containers.get().findObjects(Module.class));
		return modules;
	}

	@Override
	public void initModuleInstance(ModuleInstance moduleInstance) {
		Module module = getModule(moduleInstance);
		if (module != null) {
			ModuleContext context = new ModuleContext(moduleInstance);
			module.init(context);
		}
	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "#site.key+#position")
	public List<ModuleInstance> getModules(Site site, String position) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true).add("position",
				QueryConditions.eq(position));
		List<ModuleInstance> instances = crudService.find(ModuleInstance.class, params);
		for (ModuleInstance moduleInstance : instances) {
			moduleInstance.getParameters().size();
			initModuleInstance(moduleInstance);
		}
		return instances;

	}

	@Override
	@Cacheable(value = CACHE_NAME, key = "'positions'+#site.key")
	public List<String> getUsedPositions(Site site) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true);
		QueryBuilder queryBuilder = QueryBuilder.select(ModuleInstance.class, "m", "position").where(params)
				.groupBy("position");
		return crudService.executeQuery(queryBuilder, params);
	}

	@Override
	public List<String> getAllUsedPositions(Site site) {
		QueryParameters params = QueryParameters.with("site", site);
		QueryBuilder queryBuilder = QueryBuilder.select(ModuleInstance.class, "m", "position").where(params)
				.groupBy("position");
		return crudService.executeQuery(queryBuilder, params);
	}

	@Override
	public Module getModule(ModuleInstance instance) {
		if (instance != null && instance.getModuleId() != null) {
			for (Module module : Containers.get().findObjects(Module.class)) {
				if (instance.getModuleId().equals(module.getId())) {
					return module;
				}
			}
		}
		return null;
	}

}
