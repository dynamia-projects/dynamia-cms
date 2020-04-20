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

package tools.dynamia.cms.admin.ui.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.DynamiaSiteConnectorService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncSiteAction extends AbstractCrudAction {

    @Autowired
    private DynamiaSiteConnectorService service

    SyncSiteAction() {
        name = "Sync Site"
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {

        Site site = (Site) evt.data
        if (site != null) {
            UIMessages.showQuestion("Are you sure? This may take a while to finish.", {
                try {
                    service.sync(site)
                    UIMessages.showMessage(site + " sync completed")
                } catch (ValidationError e) {
                    UIMessages.showMessage(e.message, MessageType.WARNING)
                } catch (Exception e) {
                    UIMessages.showMessage("Error syncing site: " + e.message, MessageType.ERROR)
                    e.printStackTrace()
                }
            })
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
