/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.admin.core.zk.actions;

import com.dynamia.cms.site.core.DynamiaCMS;
import com.dynamia.cms.site.core.SiteContext;
import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.navigation.ComponentPage;
import tools.dynamia.zk.navigation.ZKNavigationManager;

/**
 *
 * @author Mario Serrano Leones
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
