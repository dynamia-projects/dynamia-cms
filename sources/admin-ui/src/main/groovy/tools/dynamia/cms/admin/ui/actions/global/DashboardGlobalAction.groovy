package tools.dynamia.cms.admin.ui.actions.global

import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.app.template.ApplicationGlobalAction
import tools.dynamia.cms.admin.ui.AdminDashboardVM

import javax.annotation.PostConstruct

@InstallAction
class DashboardGlobalAction extends ApplicationGlobalAction {

    DashboardGlobalAction() {
        image = "tachometer"
        name = "Dashboard"
        position = -1
    }


    @Override
    void actionPerformed(ActionEvent evt) {
        AdminDashboardVM.show()
    }

    @PostConstruct
    def autoshow(){
        AdminDashboardVM.show()
    }
}
