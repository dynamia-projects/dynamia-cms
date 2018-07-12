package tools.dynamia.cms.admin.ui.actions.global

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.ui.SiteManager
import tools.dynamia.commons.Messages

@InstallAction
class ApplyChangedAction extends ApplicationGlobalAction {

	@Autowired
	private SiteManager siteManager

    ApplyChangedAction() {
        name = Messages.get(ApplyChangedAction,"applyAction")
        description = Messages.get(ApplyChangedAction,"applyActionDescription")
        image = "refresh"
        setAttribute("background", "btn btn-primary btn-flat bg-light-blue-active")


    }

	@Override
    void actionPerformed(ActionEvent evt) {
		siteManager.clearCache()
    }

}
