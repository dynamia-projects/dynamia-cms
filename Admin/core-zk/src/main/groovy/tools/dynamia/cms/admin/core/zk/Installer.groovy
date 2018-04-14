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
package tools.dynamia.cms.admin.core.zk

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.annotation.Configuration
import tools.dynamia.cms.site.core.api.AdminModule
import tools.dynamia.cms.site.core.api.AdminModuleOption
import tools.dynamia.cms.site.core.api.ConfigAdminModuleOption
import tools.dynamia.crud.CrudPage
import tools.dynamia.navigation.Module
import tools.dynamia.navigation.ModuleProvider
import tools.dynamia.navigation.Page
import tools.dynamia.ui.icons.IconSize
import tools.dynamia.zk.crud.cfg.ConfigPage

@Configuration
class Installer implements BeanFactoryPostProcessor {

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory f) throws BeansException {

        f.getBeansOfType(AdminModule.class).values().stream().map { am -> new AdminModuleProvider(am) }.forEach { mod ->
            f.registerSingleton("adminModule" + System.nanoTime(), mod)
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

    }
}
