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
package tools.dynamia.cms.admin.ui

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.ConfigAdminModuleOption
import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.crud.CrudPage
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.navigation.Module
import tools.dynamia.navigation.ModuleContainer
import tools.dynamia.navigation.ModuleProvider
import tools.dynamia.navigation.Page
import tools.dynamia.ui.icons.IconSize
import tools.dynamia.zk.crud.cfg.ConfigPage

import javax.annotation.PostConstruct

@Provider
class AdminModulesInstaller {

    private static LoggingService logger = new SLF4JLoggingService(AdminModulesInstaller.class)

    @Autowired
    List<AdminModule> adminModules

    @Autowired
    private ModuleContainer moduleContainer

    @PostConstruct
    def install() {
        logger.info("Installing ${adminModules.size()} admin modules")
        adminModules.each {
            logger.info("Admin Module: $it.group - $it.name")
            def provider = new AdminModuleProvider(it)
            moduleContainer.installModule(provider.module)
        }
    }


    class AdminModuleProvider implements ModuleProvider {
        private AdminModule am

        AdminModuleProvider(AdminModule am) {
            super()
            this.am = am
        }

        @Override
        Module getModule() {
            Module module = new Module(am.group, am.name)
            module.position = 1
            if (am.image != null) {
                module.icon = am.image
            } else {
                module.icon = "fa-cube"
            }
            module.iconSize = IconSize.NORMAL

            for (AdminModuleOption option : (am.options)) {
                Page page = null

                if (option instanceof ConfigAdminModuleOption) {
                    ConfigAdminModuleOption cfg = (ConfigAdminModuleOption) option
                    page = new ConfigPage(cfg.id, cfg.name, cfg.descriptorID)
                } else {
                    page = new CrudPage(option.id, option.name, option.coreClass)
                }
                page.longName = module.name + " " + page.name
                page.addAttribute("OPTION", option)
                module.addPage(page)

            }

            return module
        }

        @Override
        String toString() {
            am.name
        }
    }
}
