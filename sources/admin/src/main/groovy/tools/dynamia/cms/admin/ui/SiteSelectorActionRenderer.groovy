/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.Events
import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.actions.ActionRenderer

/**
 *
 * @author Mario Serrano Leones
 */
class SiteSelectorActionRenderer implements ActionRenderer<SiteSelector> {

    @Override
    SiteSelector render(final Action action, final ActionEventBuilder evtBuilder) {
        final SiteSelector selector = new SiteSelector()
        selector.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

            @Override
            void onEvent(Event event) throws Exception {
                action.actionPerformed(evtBuilder.buildActionEvent(selector.selectedItem.value, null))
            }
        })

        return selector
    }

}
