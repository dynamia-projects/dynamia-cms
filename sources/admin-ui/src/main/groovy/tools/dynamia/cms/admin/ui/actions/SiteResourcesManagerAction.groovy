/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tools.dynamia.cms.admin.ui.actions

import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.modules.filemanager.FileManager
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.navigation.ComponentPage
import tools.dynamia.zk.navigation.ZKNavigationManager

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class SiteResourcesManagerAction extends AbstractCrudAction {

    SiteResourcesManagerAction() {
        name = "Manage Resources"
        image = "package"
        menuSupported = true
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        Site site = (Site) evt.data
        if (site != null) {
            FileManager mgr = new FileManager(DynamiaCMS.getSitesResourceLocation(site))
            ComponentPage page = new ComponentPage("resourcesMrg" + site.key, "Resources " + site.name, mgr)

            ZKNavigationManager.instance.currentPage = page
        }
    }

    void show(){
    	CrudActionEvent evt = new CrudActionEvent(SiteContext.get().current, null, null, null)
        actionPerformed(evt)
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Site.class)
    }

    @Override
    ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true)
    }

}
