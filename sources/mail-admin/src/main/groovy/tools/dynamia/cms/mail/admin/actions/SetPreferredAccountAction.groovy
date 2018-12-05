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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.mail.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class SetPreferredAccountAction extends AbstractCrudAction {

    @Autowired
    private MailService service

    SetPreferredAccountAction() {
        menuSupported = true
        name = "Set as preferred email account"
        image = "star"

    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        MailAccount account = (MailAccount) evt.data
        if (account != null) {
            service.preferredEmailAccount = account
            evt.controller.doQuery()
            UIMessages.showMessage("Account " + account + " set as preferred successfully")
        } else {
            UIMessages.showMessage("Select account", MessageType.WARNING)
        }
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MailAccount.class)
    }

}
