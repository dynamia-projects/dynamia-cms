/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.modules.filemanager.FileManager;

import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

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
    
    public void show(){
    	CrudActionEvent evt = new CrudActionEvent(SiteContext.get().getCurrent(), null, null, null);
    	actionPerformed(evt);
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
