package tools.dynamia.cms.admin.users.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.users.domain.UserSiteConfig
import tools.dynamia.cms.site.users.services.UserSyncService
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
