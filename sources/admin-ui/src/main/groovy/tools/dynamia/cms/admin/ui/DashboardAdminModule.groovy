package tools.dynamia.cms.admin.ui

import tools.dynamia.cms.core.api.AdminModule
import tools.dynamia.cms.core.api.AdminModuleOption
import tools.dynamia.cms.core.api.CMSModule

@CMSModule
class DashboardAdminModule implements AdminModule {

    @Override
    String getGroup() {
        return "content"
    }

    @Override
    String getName() {
        return "Content"
    }

    @Override
    String getImage() {
        return null
    }

    @Override
    AdminModuleOption[] getOptions() {
        return [
                new AdminModuleOption("dashboard", "Dashboard", "classpath:/zk/cms/dashboard.zul", true, true, "tachometer", true)
        ]
    }
}
