/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.mail;

import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.domain.util.CrudServiceListenerAdapter;
import tools.dynamia.integration.sterotypes.Listener;

/**
 *
 * @author mario
 */
@Listener
public class MailAccountCrudListener extends CrudServiceListenerAdapter<MailAccount> {

    @Override
    public void beforeCreate(MailAccount entity) {
        entity.setTimestamp(System.currentTimeMillis());
    }

    @Override
    public void beforeUpdate(MailAccount entity) {
        beforeCreate(entity);
    }

}
