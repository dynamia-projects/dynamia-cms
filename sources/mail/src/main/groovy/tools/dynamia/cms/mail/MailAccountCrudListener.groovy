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
package tools.dynamia.cms.mail

import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.sterotypes.Listener

/**
 *
 * @author mario
 */
@Listener
class MailAccountCrudListener extends CrudServiceListenerAdapter<MailAccount> {

    @Override
    void beforeCreate(MailAccount entity) {
        entity.timestamp = System.currentTimeMillis()
    }

    @Override
    void beforeUpdate(MailAccount entity) {
        beforeCreate(entity)
    }

}
