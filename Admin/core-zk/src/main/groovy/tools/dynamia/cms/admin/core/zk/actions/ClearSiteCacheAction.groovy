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
package tools.dynamia.cms.admin.core.zk.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class ClearSiteCacheAction extends AbstractCrudAction {

	@Autowired
	private SiteService service

    ClearSiteCacheAction() {
        name = "Clear Cache"
        image = "refresh"
        menuSupported = true
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Site site = (Site) evt.data
        if (site != null) {
			service.clearCache(site)
            UIMessages.showMessage("Site Cache cleared successful")
        } else {
			UIMessages.showMessage("Select site", MessageType.WARNING)
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
