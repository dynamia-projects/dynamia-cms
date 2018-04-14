package tools.dynamia.cms.admin.core.zk.actions.global

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.core.zk.SiteManager

@InstallAction
class SettingsAction extends ApplicationGlobalAction {

	@Autowired
	private SiteManager siteManager

    SettingsAction() {
        name = "Settings"
        image = "cog"
        position = 2
        setAttribute("background", "btn btn-warning btn-flat bg-yellow ")
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		siteManager.edit()
    }

}
