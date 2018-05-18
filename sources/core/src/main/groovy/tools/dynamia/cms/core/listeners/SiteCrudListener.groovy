/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.core.listeners

import tools.dynamia.cms.core.SiteInitializer
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.domain.util.CrudServiceListenerAdapter
import tools.dynamia.integration.Containers
import tools.dynamia.integration.sterotypes.Listener

/**
 *
 * @author mario
 */
@Listener
class SiteCrudListener extends CrudServiceListenerAdapter<Site> {

    @Override
    void beforeCreate(Site site) {
        Containers.get().findObjects(SiteInitializer.class).forEach { s -> s.init(site) }
    }

    @Override
    void afterCreate(Site site) {
        Containers.get().findObjects(SiteInitializer.class).forEach { s -> s.postInit(site) }
    }

}
