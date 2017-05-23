package tools.dynamia.cms.admin.users.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.cms.site.users.services.UserService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class DisableUserAction extends AbstractCrudAction {

	@Autowired
	private UserService service;

	public DisableUserAction() {
		setName("Disable User");
		setGroup(ActionGroup.get("user"));
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(User.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {

		User user = (User) evt.getData();

		if (user != null && user.isEnabled()) {

			UIMessages.showQuestion("Confirm you want disable user: " + user + "?", () -> {
				try {
					service.disableUser(user);
					UIMessages.showMessage("User disabled successfull");
					evt.getController().doQuery();
				} catch (ValidationError e) {
					UIMessages.showMessage(e.getMessage(), MessageType.ERROR);
				}
			});
		} else {
			UIMessages.showMessage("Select user first", MessageType.WARNING);
		}

	}

}
