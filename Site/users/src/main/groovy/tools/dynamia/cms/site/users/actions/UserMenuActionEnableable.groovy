package tools.dynamia.cms.site.users.actions;

import tools.dynamia.cms.site.users.domain.User;

public interface UserMenuActionEnableable extends UserMenuAction{

	boolean isEnabled(User currentUser);
}
