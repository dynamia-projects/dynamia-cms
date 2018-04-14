package tools.dynamia.cms.admin.core.zk.actions.global

import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.core.zk.actions.SiteResourcesManagerAction
import tools.dynamia.integration.Containers

@InstallAction
class ResourcesGlobalAction extends ApplicationGlobalAction {

	ResourcesGlobalAction() {
        name = "Resources"
        image = "cloud"
        position = 3
        setAttribute("background", "btn btn-success btn-flat bg-green")
    }

	@Override
    void actionPerformed(ActionEvent evt) {
		SiteResourcesManagerAction action = Containers.get().findObject(SiteResourcesManagerAction.class)
        action.show()
    }

}
