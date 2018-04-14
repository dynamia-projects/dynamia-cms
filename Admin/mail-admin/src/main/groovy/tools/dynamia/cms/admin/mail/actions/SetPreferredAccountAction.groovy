/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.mail.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.mail.domain.MailAccount
import tools.dynamia.cms.site.mail.services.MailService
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
public class SetPreferredAccountAction extends AbstractCrudAction {

    @Autowired
    private MailService service;

    public SetPreferredAccountAction() {
        setMenuSupported(true);
        setName("Set as preferred email account");
        setImage("star");

    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        MailAccount account = (MailAccount) evt.getData();
        if (account != null) {
            service.setPreferredEmailAccount(account);
            evt.getController().doQuery();
            UIMessages.showMessage("Account " + account + " set as preferred successfully");
        } else {
            UIMessages.showMessage("Select account", MessageType.WARNING);
        }
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MailAccount.class);
    }

}
