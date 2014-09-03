package com.dynamia.cms.site.core;

import java.util.List;

import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.impl.ModulesService;

public class CMSModules {

	private Site site;
	private ModulesService service;

	public CMSModules(Site site, ModulesService service) {
		super();
		this.site = site;
		this.service = service;
	}

	public List<ModuleInstance> getInstances(String position) {
		List<ModuleInstance> instances = service.getModules(site, position);
		return instances;
	}

	public List<String> getUsedPositions() {
		return service.getUsedPositions(site);
	}

	public String getTemplateViewName(ModuleInstance instance) {
		Module module = service.getModule(instance);
		if (module != null) {
			return module.getTemplateViewName();
		} else {
			return null;
		}
	}

	public void init(ModuleInstance instance) {
		service.initModuleInstance(instance);
	}

}
