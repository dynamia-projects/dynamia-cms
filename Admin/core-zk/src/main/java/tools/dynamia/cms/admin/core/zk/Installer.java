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
package tools.dynamia.cms.admin.core.zk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tools.dynamia.cms.site.core.api.AdminModule;
import tools.dynamia.cms.site.core.api.AdminModuleOption;
import tools.dynamia.cms.site.core.api.ConfigAdminModuleOption;
import org.zkoss.lang.Library;

import tools.dynamia.crud.CrudPage;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.Page;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.zk.crud.cfg.ConfigPage;

@Component("CoreModule")
public class Installer implements ModuleProvider {

    @Autowired
    private List<AdminModule> adminModules;

    public Installer() {
        Library.setProperty("org.zkoss.theme.preferred", "atlantic");
    }

    @Override
    public Module getModule() {
        Module module = new Module("admin", "Administration");
        module.setPosition(1);
        module.setIcon("icons:admin");
        module.setIconSize(IconSize.NORMAL);

        if (adminModules != null) {
            for (AdminModule am : adminModules) {
                PageGroup pg = module.getPageGroupById(am.getGroup());
                if (pg == null) {
                    pg = new PageGroup(am.getGroup(), am.getName());
                    module.addPageGroup(pg);
                }
                for (AdminModuleOption option : am.getOptions()) {
                    Page page = null;

                    if (option instanceof ConfigAdminModuleOption) {
                        ConfigAdminModuleOption cfg = (ConfigAdminModuleOption) option;
                        page = new ConfigPage(cfg.getId(), cfg.getName(), cfg.getDescriptorID());
                    } else {
                        page = new CrudPage(option.getId(), option.getName(), option.getCoreClass());
                    }
                    page.addAttribute("OPTION", option);
                    pg.addPage(page);
                }
            }
        }

        return module;
    }
}
