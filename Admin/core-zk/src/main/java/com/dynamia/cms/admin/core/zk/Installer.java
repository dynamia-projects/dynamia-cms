/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;
import com.dynamia.cms.site.core.api.ConfigAdminModuleOption;

import tools.dynamia.crud.CrudPage;
import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.ModuleProvider;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.zk.crud.cfg.ConfigPage;

@Component("CoreModule")
public class Installer implements ModuleProvider {

	@Autowired
	private List<AdminModule> adminModules;

	@Override
	public Module getModule() {
		Module module = new Module("admin", "Administration");
		module.setPosition(1);
		module.setIcon("icons:tool");

		if (adminModules != null) {
			for (AdminModule am : adminModules) {
				PageGroup pg = module.getPageGroupById(am.getGroup());
				if (pg == null) {
					pg = new PageGroup(am.getGroup(), am.getName());
					module.addPageGroup(pg);
				}
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
