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
package tools.dynamia.cms.admin.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.cms.templates.CacheController
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.crud.CrudView

@Component("siteMgr")
@Scope("session")
class SiteManager {

	@Autowired
	private CacheController cacheController

    @Autowired
	private CrudService crudService

    void clearCache() {
		Site site = SiteContext.get().current
        if (site != null) {
			cacheController.clearAllCache()
            cacheController.clearTemplate()
            UIMessages.showMessage("Site Cache cleared successfull")
        }

	}

    void edit() {
		Site site = crudService.reload(SiteContext.get().current)
        CrudView.showUpdateView("Edit " + site, Site.class, site)
    }

}
