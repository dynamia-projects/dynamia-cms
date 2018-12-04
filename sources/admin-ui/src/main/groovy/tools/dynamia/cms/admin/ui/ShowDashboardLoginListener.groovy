package tools.dynamia.cms.admin.ui

import tools.dynamia.cms.users.LoginListener
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.integration.sterotypes.Listener
import tools.dynamia.zk.util.ZKUtil

@Listener
class ShowDashboardLoginListener implements LoginListener {

    @Override
    void onLoginSuccess(User user) {
        if (ZKUtil.inEventListener && (user.profile == UserProfile.EDITOR || user.profile == UserProfile.ADMIN)) {
            AdminDashboardVM.show()
        }
    }
}
