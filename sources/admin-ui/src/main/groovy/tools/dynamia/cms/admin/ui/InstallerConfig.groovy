/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
