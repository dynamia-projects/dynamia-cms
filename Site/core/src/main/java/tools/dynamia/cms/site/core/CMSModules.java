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
package tools.dynamia.cms.site.core;

import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tools.dynamia.cms.site.core.api.Module;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.impl.ModulesService;

public class CMSModules {

	private Site site;
	private ModulesService service;
	private Set<ModuleInstance> activeInstances = new HashSet<>();
	private Set<Module> activeModules = new HashSet<>();

	public CMSModules(Site site, ModulesService service) {
		super();
		this.site = site;
		this.service = service;
	}

	public List<ModuleInstance> getInstances(String position) {
		List<ModuleInstance> instances = filter(service.getModules(site, position));

		activeInstances.addAll(instances);
		for (ModuleInstance moduleInstance : instances) {
			activeModules.add(service.getModule(moduleInstance));
		}
		return instances;
	}

	private List<ModuleInstance> filter(List<ModuleInstance> modules) {
		try {

			String currentPath = CMSUtil.getCurrentRequest().getPathInfo();
			return modules.stream()
					.filter(m -> m.isPathIncluded(currentPath))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modules;
	}

	public List<String> getUsedPositions() {
		return service.getUsedPositions(site);
	}

	public boolean isUsed(String position) {
		if (position != null) {
			for (String usedPosition : getUsedPositions()) {
				if (position.equals(usedPosition)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getTemplateViewName(ModuleInstance instance) {
		String view = "error/modulenotfound";

		if (instance.getCustomView() != null && !instance.getCustomView().isEmpty()) {
			view = instance.getCustomView();
		} else {
			Module module = service.getModule(instance);
			if (module != null) {
				view = module.getTemplateViewName();
			}
		}

		return view;
	}

	public void init(ModuleInstance instance) {
		service.initModuleInstance(instance);
		activeInstances.add(instance);

	}

	public Set<ModuleInstance> getActiveInstances() {
		return activeInstances;
	}

	public Set<Module> getActiveModules() {
		return activeModules;
	}
}
