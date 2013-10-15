/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import org.springframework.stereotype.Component;

import com.dynamia.tools.web.crud.CrudPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;
import com.dynamia.tools.web.navigation.Page;
import com.dynamia.tools.web.navigation.PageGroup;


@Component("CoreModule")
public class Installer implements ModuleProvider {

    @Override
    public Module getModule() {
        Module module = new Module("moduleId", "Module Name");
        module.setPosition(10);
        module.setIcon("icons:module");
        {
            PageGroup pg = new PageGroup("group", "Group1");
            module.addPageGroup(pg);
            {
                //pg.addPage(new CrudPage("zonas", "Zonas", Domain.class));
                
            }
        }
                

        return module;
    }
}
