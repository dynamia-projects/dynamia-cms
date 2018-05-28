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
package tools.dynamia.cms.core


import tools.dynamia.cms.core.api.Module
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.impl.ModulesService

class CMSModules {

    private Site site
    private ModulesService service
    private Set<ModuleInstance> activeInstances = new HashSet<>()
    private Set<Module> activeModules = new HashSet<>()

    CMSModules(Site site, ModulesService service) {
        super()
        this.site = site
        this.service = service
    }

    List<ModuleInstance> getInstances(String position) {
        List<ModuleInstance> instances = filter(service.getModules(site, position))

        activeInstances.addAll(instances)
        for (ModuleInstance moduleInstance : instances) {
            Module module = service.getModule(moduleInstance)
            if (module == null) {
                throw new CMSException("Cannot load module $moduleInstance.moduleId. Check dependencies.")
            }

            if (!module.cacheable) {
                service.initModuleInstance(moduleInstance)
            }
            activeModules.add(module)
        }
        return instances
    }

    private List<ModuleInstance> filter(List<ModuleInstance> modules) {
        try {

            String currentPath = CMSUtil.currentRequest.requestURI
            return modules.findAll { m -> m.isPathIncluded(currentPath) }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return modules
    }

    List<String> getUsedPositions() {
        return service.getUsedPositions(site)
    }

    boolean isUsed(String position) {
        if (position != null) {
            for (String usedPosition : (usedPositions)) {
                if (position.equals(usedPosition)) {
                    return true
                }
            }
        }
        return false
    }

    String getTemplateViewName(ModuleInstance instance) {
        String view = "error/modulenotfound"

        if (instance.customView != null && !instance.customView.empty) {
            view = instance.customView
        } else {
            Module module = service.getModule(instance)
            if (module != null) {
                view = module.templateViewName
            }
        }

        return view
    }

    void init(ModuleInstance instance) {
        service.initModuleInstance(instance)
        activeInstances.add(instance)
    }

    Set<ModuleInstance> getActiveInstances() {
        return activeInstances
    }

    Set<Module> getActiveModules() {
        return activeModules
    }
}
