/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.ConfigAdminModuleOption;
import com.dynamia.tools.web.cfg.ConfigPage;
import org.springframework.stereotype.Component;

import com.dynamia.tools.web.crud.CrudPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;
import com.dynamia.tools.web.navigation.PageGroup;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Component("CoreModule")
public class Installer implements ModuleProvider {

    @Autowired
    private List<AdminModule> adminModules;

    @Override
    public Module getModule() {
        Module module = new Module("admin", "Administration");
        module.setPosition(1);
        module.setIcon("icons:module");

        if (adminModules != null) {
            for (AdminModule am : adminModules) {
                PageGroup pg = new PageGroup(am.getGroup(), am.getName());
                module.addPageGroup(pg);
                for (AdminModuleOption option : am.getOptions()) {
                    if (option instanceof ConfigAdminModuleOption) {
                        ConfigAdminModuleOption cfg = (ConfigAdminModuleOption) option;
                        pg.addPage(new ConfigPage(cfg.getId(), cfg.getName(), cfg.getDescriptorID()));
                    } else {
                        pg.addPage(new CrudPage(option.getId(), option.getName(), option.getCoreClass()));
                    }
                }
            }
        }

        return module;
    }
}
