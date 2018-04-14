package tools.dynamia.cms.admin.users.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionGroup
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class EnableUserAction extends AbstractCrudAction {

    @Autowired
    private UserService service

    EnableUserAction() {
        name = "Enable User"
        group = ActionGroup.get("user")
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(User.class)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {

        User user = (User) evt.data

        if (user != null && !user.enabled) {

            UIMessages.showQuestion("Confirm you want enable user: " + user + "?", {
                try {
                    service.enableUser(user)
                    UIMessages.showMessage("User enabled successfull")
                    evt.controller.doQuery()
                } catch (ValidationError e) {
                    UIMessages.showMessage(e.message, MessageType.ERROR)
                }
            })
        } else {
            UIMessages.showMessage("Select user first", MessageType.WARNING)
        }

    }

}
