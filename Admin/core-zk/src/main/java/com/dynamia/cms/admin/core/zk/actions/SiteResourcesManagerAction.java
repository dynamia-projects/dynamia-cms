/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.modules.filemanager.FileManager;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.ActionRenderer;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;
import com.dynamia.tools.web.crud.actions.renderers.ToolbarbuttonActionRenderer;
import com.dynamia.tools.web.navigation.ComponentPage;
import com.dynamia.tools.web.navigation.ZKNavigationManager;

/**
 *
 * @author mario_2
 */
@InstallAction
public class SiteResourcesManagerAction extends AbstractCrudAction {

    public SiteResourcesManagerAction() {
        setName("Manage Resources");
        setImage("package");
        setMenuSupported(true);
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Site site = (Site) evt.getData();
        if (site != null) {
            FileManager mgr = new FileManager(DynamiaCMS.getSitesResourceLocation(site));
            ComponentPage page = new ComponentPage("resourcesMrg" + site.getKey(), "Resources " + site.getName(), mgr);

            ZKNavigationManager.getInstance().setCurrentPage(page);
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

    @Override
    public ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true);
    }

}
