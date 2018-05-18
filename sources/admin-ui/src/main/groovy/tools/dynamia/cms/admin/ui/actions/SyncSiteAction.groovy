package tools.dynamia.cms.admin.ui.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.DynamiaSiteConnectorService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncSiteAction extends AbstractCrudAction {

    @Autowired
    private DynamiaSiteConnectorService service

    SyncSiteAction() {
        name = "Sync Site"
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {

        Site site = (Site) evt.data
        if (site != null) {
            UIMessages.showQuestion("Are you sure? This may take a while to finish.", {
                try {
                    service.sync(site)
                    UIMessages.showMessage(site + " sync completed")
                } catch (ValidationError e) {
                    UIMessages.showMessage(e.message, MessageType.WARNING)
                } catch (Exception e) {
                    UIMessages.showMessage("Error syncing site: " + e.message, MessageType.ERROR)
                    e.printStackTrace()
                }
            })
        }

    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Site.class)
    }
}
