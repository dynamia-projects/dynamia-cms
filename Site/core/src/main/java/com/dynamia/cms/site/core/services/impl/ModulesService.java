package com.dynamia.cms.site.core.services.impl;

import java.util.List;

import com.dynamia.cms.site.core.api.Module;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.Site;

public interface ModulesService {

	/**
	 * Return all enabled modules instances in an specific position
	 * 
	 * @param site
	 * @param position
	 * @return
	 */
	public List<ModuleInstance> getModules(Site site, String position);

	/**
	 * Return a list of used positions by enabled modules instances
	 * 
	 * @param site
	 * @return
	 */
	public List<String> getUsedPositions(Site site);

	public abstract Module getModule(ModuleInstance instance);

	public abstract void initModuleInstance(ModuleInstance moduleInstance);

	List<Module> getInstalledModules();

}