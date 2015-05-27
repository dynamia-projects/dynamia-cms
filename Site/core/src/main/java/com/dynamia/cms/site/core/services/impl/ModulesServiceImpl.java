package com.dynamia.cms.site.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import com.dynamia.tools.domain.util.QueryBuilder;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Service;

@Service
public class ModulesServiceImpl implements ModulesService {

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
	@Cacheable(value = "modules", key = "#site.key+#position")
	public List<ModuleInstance> getModules(Site site, String position) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true).add("position", position);
		List<ModuleInstance> instances = crudService.find(ModuleInstance.class, params);
		for (ModuleInstance moduleInstance : instances) {
			moduleInstance.getParameters().size();
			initModuleInstance(moduleInstance);
		}
		return instances;

	}

	@Override
	@Cacheable(value = "modules", key = "positions+#site.key")
	public List<String> getUsedPositions(Site site) {
		QueryParameters params = QueryParameters.with("site", site).add("enabled", true);
		QueryBuilder queryBuilder = QueryBuilder.select(ModuleInstance.class, "m", "position").where(params).groupBy("position");
		return crudService.executeQuery(queryBuilder, params);
	}

	@Override
	public Module getModule(ModuleInstance instance) {
		if (instance != null && instance.getModuleId()!=null) {
			for (Module module : Containers.get().findObjects(Module.class)) {
				if (instance.getModuleId().equals(module.getId())) {
					return module;
				}
			}
		}
		return null;
	}

}
