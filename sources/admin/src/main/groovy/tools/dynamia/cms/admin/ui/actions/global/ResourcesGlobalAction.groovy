/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.cms.admin.ui.actions.global

import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.ui.actions.SiteResourcesManagerAction
import tools.dynamia.commons.Messages
import tools.dynamia.integration.Containers

@InstallAction
class ResourcesGlobalAction extends ApplicationGlobalAction {

	ResourcesGlobalAction() {
        name = Messages.get(ResourcesGlobalAction,"resourcesAction")
        image = "cloud"
        position = 3
        setAttribute("background", "btn btn-success btn-flat bg-green")
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		SiteResourcesManagerAction action = Containers.get().findObject(SiteResourcesManagerAction.class)
        action.show()
    }

}
