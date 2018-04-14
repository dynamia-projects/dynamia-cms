/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.mail

import tools.dynamia.cms.site.mail.domain.MailAccount
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
