/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.crud.CrudPage;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.zk.crud.cfg.ConfigPage;

/**
 *
 * @author mario
 */
@Component("CMSInstallerConfigModule")
public class InstallerConfig implements ModuleProvider {

	@Override
	public Module getModule() {
		Module module = new Module("system", "System");
		module.setIcon("icons:system");
		module.setIconSize(IconSize.NORMAL);
		module.addPage(new CrudPage("sites", "Sites", Site.class));
		module.addPage(new ConfigPage("cmsconfig", "Configuration", "CMSConfig"));

		return module;
	}

}
