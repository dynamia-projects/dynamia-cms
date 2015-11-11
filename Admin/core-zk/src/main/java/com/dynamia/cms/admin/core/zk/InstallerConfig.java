/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.web.cfg.ConfigPage;
import com.dynamia.tools.web.crud.CrudPage;
import com.dynamia.tools.web.navigation.Module;
import com.dynamia.tools.web.navigation.ModuleProvider;

/**
 *
 * @author mario
 */
@Component("CMSInstallerConfigModule")
public class InstallerConfig implements ModuleProvider {

	@Override
	public Module getModule() {
		Module m = new Module("system", "System");
		m.addPage(new CrudPage("sites", "Sites", Site.class));
		m.addPage(new ConfigPage("cmsconfig", "Configuration", "CMSConfig"));

		return m;
	}

}
