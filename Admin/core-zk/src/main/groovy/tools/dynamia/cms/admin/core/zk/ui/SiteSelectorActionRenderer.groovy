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
package tools.dynamia.cms.admin.core.zk.ui

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
