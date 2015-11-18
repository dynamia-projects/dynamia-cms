package com.dynamia.cms.admin.core.zk.controllers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import com.dynamia.cms.site.core.api.AdminModule;
import com.dynamia.cms.site.core.api.AdminModuleOption;

import tools.dynamia.integration.Containers;
import tools.dynamia.navigation.NavigationRestrictions;
import tools.dynamia.navigation.Page;
import tools.dynamia.zk.navigation.ZKNavigationManager;

public class AdminModuleShortcutVM {

	public List<AdminModuleOption> getShortcuts() {

		List<AdminModuleOption> shortcuts = new ArrayList<>();

		for (AdminModule module : Containers.get().findObjects(AdminModule.class)) {
			for (AdminModuleOption option : module.getOptions()) {
				if (option.isShortcut()) {
					Page dummy = new Page("x", "x", "x");
					dummy.addAttribute("OPTION", option);
					if (NavigationRestrictions.allowAccess(dummy)) {
						option.setPath("admin/" + module.getGroup() + "/" + option.getId());
						shortcuts.add(option);
					}
				}
			}
		}

		return shortcuts;

	}

	@Command
	public void navigateTo(@BindingParam("path") String path) {
		ZKNavigationManager.getInstance().navigateTo(path);
	}

}
