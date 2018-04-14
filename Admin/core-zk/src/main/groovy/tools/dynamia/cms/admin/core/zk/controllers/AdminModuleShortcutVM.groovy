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
package tools.dynamia.cms.admin.core.zk.controllers

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import tools.dynamia.cms.site.core.api.AdminModule
import tools.dynamia.cms.site.core.api.AdminModuleOption
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
