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
package tools.dynamia.cms.admin.ui

import org.springframework.beans.factory.annotation.Autowired
import org.zkoss.util.Locales
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
            module.setBaseClass(am.getClass())
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
                } else if(option.coreClass!=null){
                    page = new CrudPage(option.id, option.name, option.coreClass)
                }else{
                    page = new Page(option.id,option.name,option.path)
                }
                page.longNameSupplier = {
                    return "${module.getLocalizedName(Locales.current)} / ${page.getLocalizedName(Locales.current)}".toString()
                }
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
