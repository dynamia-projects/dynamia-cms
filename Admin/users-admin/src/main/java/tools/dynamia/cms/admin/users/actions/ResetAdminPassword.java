package tools.dynamia.cms.admin.users.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.services.UserService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class ResetAdminPassword extends AbstractCrudAction {

	@Autowired
	private UserService service;

	public ResetAdminPassword() {
		setName("Reset Admin Password");
		setImage("user");
		setMenuSupported(true);
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Site.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		Site site = (Site) evt.getData();
		if (site != null) {
			String admin = "admin@" + site.getKey() + ".login";
			User user = service.getUser(site, admin);
			if (user != null) {
				ZKUtil.showInputDialog("Enter new Password for " + admin, String.class, e -> {
					String password = (String) e.getData();
					if (password != null && !password.isEmpty()) {
						service.resetPassword(user, password, password);
						UIMessages.showMessage("Password reset succesfully");
					}
				});
			}
		} else {
			UIMessages.showMessage("Select site first", MessageType.ERROR);
		}

	}

}
