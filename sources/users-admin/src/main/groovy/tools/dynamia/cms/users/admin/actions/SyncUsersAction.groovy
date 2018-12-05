/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
