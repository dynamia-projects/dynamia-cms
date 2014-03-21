/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;

/**
 *
 * @author mario_2
 */
@InstallAction
public class SiteSelectorAction extends AbstractCrudAction {

    public SiteSelectorAction() {
        setName("Select Site");
    }

    
    
    @Override
    public void actionPerformed(CrudActionEvent evt) {
        Site site = (Site) evt.getData();
        if (site != null) {
            evt.getController().setParemeter("site", site);
            evt.getController().doQuery();
        }
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(SiteAware.class);
    }

}
