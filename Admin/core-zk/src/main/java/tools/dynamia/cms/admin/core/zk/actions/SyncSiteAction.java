package tools.dynamia.cms.admin.core.zk.actions;

import org.springframework.beans.factory.annotation.Autowired;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.DynamiaSiteConnectorService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class SyncSiteAction extends AbstractCrudAction {

    @Autowired
    private DynamiaSiteConnectorService service;

    public SyncSiteAction() {
        setName("Sync Site");
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {

        Site site = (Site) evt.getData();
        if (site != null) {
            UIMessages.showQuestion("Are you sure? This may take a while to finish.", () -> {
                try {
                    service.sync(site);
                    UIMessages.showMessage(site + " sync completed");
                } catch (ValidationError e) {
                    UIMessages.showMessage(e.getMessage(), MessageType.WARNING);
                } catch (Exception e) {
                    UIMessages.showMessage("Error syncing site: " + e.getMessage(), MessageType.ERROR);
                    e.printStackTrace();
                }
            });
        }

    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Site.class);
    }
}
