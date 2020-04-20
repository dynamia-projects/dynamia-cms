/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
