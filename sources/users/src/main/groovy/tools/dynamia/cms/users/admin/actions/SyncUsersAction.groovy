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

package tools.dynamia.cms.users.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.users.domain.UserSiteConfig
import tools.dynamia.cms.users.services.UserSyncService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncUsersAction extends AbstractCrudAction {

	@Autowired
	private UserSyncService service

    SyncUsersAction() {
        name = "Sync User"
        image = "sync"
        menuSupported = true
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(UserSiteConfig.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		UserSiteConfig cfg = (UserSiteConfig) evt.data
        if (cfg != null) {
			UIMessages.showQuestion("Are you sure to sync all users? This may take serveral minutes", {
				try {
					service.syncUsers(cfg, new HashMap<>())
                    UIMessages.showMessage("Sync Successfull")
                    evt.controller.doQuery()
                } catch (Exception e) {
					UIMessages.showMessage("Error sync users: " + e.message, MessageType.ERROR)
                    e.printStackTrace()
                }
			})
        }

	}

}
