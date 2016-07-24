package tools.dynamia.cms.admin.users.actions;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.users.domain.UserSiteConfig;
import tools.dynamia.cms.site.users.services.UserSyncService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class SyncUsersAction extends AbstractCrudAction {

	@Autowired
	private UserSyncService service;

	public SyncUsersAction() {
		setName("Sync User");
		setImage("sync");
		setMenuSupported(true);
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(UserSiteConfig.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		UserSiteConfig cfg = (UserSiteConfig) evt.getData();
		if (cfg != null) {
			UIMessages.showQuestion("Are you sure to sync all users? This may take serveral minutes", () -> {
				try {
					service.syncUsers(cfg, new HashMap<>());
					UIMessages.showMessage("Sync Successfull");
					evt.getController().doQuery();
				} catch (Exception e) {
					UIMessages.showMessage("Error sync users: " + e.getMessage(), MessageType.ERROR);
					e.printStackTrace();
				}
			});
		}

	}

}
