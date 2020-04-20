/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.admin.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.api.SiteCacheListener
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.Containers
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.crud.CrudView

@Component("siteMgr")
@Scope("session")
class SiteManager {

    @Autowired
    private CacheManager cacheManager

    @Autowired
    private CrudService crudService

    void clearCache() {
        Site site = SiteContext.get().current
        if (site != null) {
            Containers.get().findObjects(SiteCacheListener)?.each { it.beforeCacheClear(site) }

            cacheManager.getCacheNames().each {
                cacheManager.getCache(it).clear()
            }

            Containers.get().findObjects(SiteCacheListener)?.each { it.afterCacheClear(site) }
            UIMessages.showMessage("Site Cache cleared successfull")
        }

    }

    void edit() {
        Site site = crudService.reload(SiteContext.get().current)
        CrudView.showUpdateView("Edit " + site, Site.class, site)
    }

}
