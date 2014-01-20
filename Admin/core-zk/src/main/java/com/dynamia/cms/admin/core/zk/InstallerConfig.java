/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import com.dynamia.tools.domain.query.Parameter;
import com.dynamia.tools.web.cfg.ConfigPage;
import com.dynamia.tools.web.crud.CrudPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;
import com.dynamia.tools.web.navigation.PageGroup;
import org.springframework.stereotype.Component;

/**
 *
 * @author mario
 */
@Component("CMSInstallerConfigModule")
public class InstallerConfig implements ModuleProvider {

    @Override
    public Module getModule() {
        Module m = new Module("system", "System");
        PageGroup pg = new PageGroup("config", "Configuration");
        m.addPageGroup(pg);
        {
            pg.addPage(new ConfigPage("cmsconfig", "Default Configuration", "CMSConfig"));
            pg.addPage(new CrudPage("params", "Global Parameters", Parameter.class));
        }

        return m;
    }

}
