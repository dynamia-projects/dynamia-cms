package tools.dynamia.cms.admin.core.zk.actions.global

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.core.zk.SiteManager

@InstallAction
class ApplyChangedAction extends ApplicationGlobalAction {

	@Autowired
	private SiteManager siteManager

    ApplyChangedAction() {
        name = "Apply"
        description = "All changes are visible in site"
        image = "refresh"
        setAttribute("background", "btn btn-primary btn-flat bg-light-blue-active")


    }

	@Override
    void actionPerformed(ActionEvent evt) {
		siteManager.clearCache()
    }

}
