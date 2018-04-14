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
package tools.dynamia.cms.site.core.services.impl

import tools.dynamia.cms.site.core.api.Module
import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.domain.Site

interface ModulesService {

	/**
	 * Return all enabled modules instances in an specific position
	 * 
	 * @param site
	 * @param position
	 * @return
	 */
    List<ModuleInstance> getModules(Site site, String position)

    /**
	 * Return a list of used positions by enabled modules instances
	 * 
	 * @param site
	 * @return
	 */
	List<String> getUsedPositions(Site site)

    abstract Module getModule(ModuleInstance instance)

    abstract void initModuleInstance(ModuleInstance moduleInstance)

    List<Module> getInstalledModules()

    List<String> getAllUsedPositions(Site site)

}