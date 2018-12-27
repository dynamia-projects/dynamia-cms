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
package tools.dynamia.cms.admin.ui.controllers

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.integration.Containers
import tools.dynamia.navigation.NavigationRestrictions
import tools.dynamia.navigation.Page
import tools.dynamia.zk.navigation.ZKNavigationManager

class AdminModuleShortcutVM {

	List<AdminModuleOption> getShortcuts() {

		List<AdminModuleOption> shortcuts = new ArrayList<>()

        for (AdminModule module : Containers.get().findObjects(AdminModule.class)) {
			for (AdminModuleOption option : (module.options)) {
				if (option.shortcut) {
					Page dummy = new Page("x", "x", "x")
                    dummy.addAttribute("OPTION", option)
                    if (NavigationRestrictions.allowAccess(dummy)) {
                        option.path = "admin/" + module.group + "/" + option.id
                        shortcuts.add(option)
                    }
				}
			}
		}

		return shortcuts

    }

	@Command
    void navigateTo(@BindingParam("path") String path) {
		ZKNavigationManager.instance.navigateTo(path)
    }

}