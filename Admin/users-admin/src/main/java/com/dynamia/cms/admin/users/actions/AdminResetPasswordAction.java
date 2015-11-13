package com.dynamia.cms.admin.users.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.services.UserService;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.util.ZKUtil;

@InstallAction
public class AdminResetPasswordAction extends AbstractCrudAction {

	@Autowired
	private UserService userService;

	public AdminResetPasswordAction() {
		setName("Reset Password");
		setImage("refresh");
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
		if (user != null) {

			ZKUtil.showInputDialog("Ingrese nuevo passwrod", String.class, e -> {
				String newpassword = (String) e.getData();
				try {
					userService.resetPassword(user, newpassword, newpassword);
					UIMessages.showMessage("Password de usuario " + user.getUsername() + " reiniciado exitosamente");
				} catch (ValidationError ex) {
					UIMessages.showMessage(ex.getMessage(), MessageType.WARNING);

				} catch (Exception ex) {
					UIMessages.showMessage("Error al reiniciar password de usuario " + user.getUsername() + ": " + ex.getMessage(),
							MessageType.ERROR);
					ex.printStackTrace();
				}
			});
		}
	}

}
