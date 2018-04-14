package tools.dynamia.cms.admin.users.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ResetAdminPassword extends AbstractCrudAction {

    @Autowired
    private UserService service

    ResetAdminPassword() {
        name = "Reset Admin Password"
        image = "user"
        menuSupported = true
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Site.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        Site site = (Site) evt.data
        if (site != null) {
            String admin = "admin@" + site.key + ".login"
            User user = service.getUser(site, admin)
            if (user != null) {
                ZKUtil.showInputDialog("Enter new Password for " + admin, String.class, {
                    String password = (String) e.getData()
                    if (password != null && !password.empty) {
                        service.resetPassword(user, password, password)
                        UIMessages.showMessage("Password reset succesfully")
                    }
                })
            }
        } else {
            UIMessages.showMessage("Select site first", MessageType.ERROR)
        }

    }

}
