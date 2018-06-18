package tools.dynamia.cms.users.admin

import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSExtension
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.navigation.NavigationElement
import tools.dynamia.navigation.NavigationRestriction

@CMSExtension
class UserNavigationRestriction implements NavigationRestriction {

    @Override
    int getOrder() {
        return 1
    }

    @Override
    Boolean allowAccess(NavigationElement element) {
        AdminModuleOption option = (AdminModuleOption) element.getAttribute("OPTION")
        if (option != null) {
            User user = UserHolder.get().current
            if (user != null) {
                return (option.editorAllowed && (user.profile == UserProfile.EDITOR || user.profile == UserProfile.ADMIN)) || (option.adminAllowed && user.profile == UserProfile.ADMIN)
            } else {
                return null
            }
        } else {
            return null
        }
    }

}