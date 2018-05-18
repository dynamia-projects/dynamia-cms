package tools.dynamia.cms.users.actions

import tools.dynamia.cms.users.domain.User

interface UserMenuActionEnableable extends UserMenuAction{

	boolean isEnabled(User currentUser)
}
