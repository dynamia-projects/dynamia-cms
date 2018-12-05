/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.admin.ui

import org.springframework.stereotype.Component
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.crud.CrudPage
import tools.dynamia.navigation.Module
import tools.dynamia.navigation.ModuleProvider
import tools.dynamia.ui.icons.IconSize
import tools.dynamia.zk.crud.cfg.ConfigPage

/**
 * @author Mario Serrano Leones
 */
@Component("CMSInstallerConfigModule")
class InstallerConfig implements ModuleProvider {

    @Override
    Module getModule() {
        Module module = new Module("system", "System")
        module.icon = "fa-cogs"
        module.iconSize = IconSize.NORMAL
        module.addPage(new CrudPage("sites", "Sites", Site.class))
        module.addPage(new ConfigPage("cmsconfig", "Configuration", "CMSConfig"))
        module.addPage(new GlobalResourcesPage("globalResources", "Global Resources"))

        return module
    }

}
