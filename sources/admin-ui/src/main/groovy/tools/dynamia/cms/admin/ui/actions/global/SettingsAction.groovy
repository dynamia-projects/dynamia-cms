package tools.dynamia.cms.admin.ui.actions.global

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.ui.SiteManager
import tools.dynamia.commons.Messages

@InstallAction
class SettingsAction extends ApplicationGlobalAction {

    @Autowired
    private SiteManager siteManager

    SettingsAction() {
        name = Messages.get(SettingsAction,"settingsAction")
        image = "cog"
        position = 2
        setAttribute("background", "btn bg-orange btn-flat")
    }

    @Override
    void actionPerformed(ActionEvent evt) {
        siteManager.edit()
    }

}
