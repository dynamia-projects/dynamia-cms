/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import com.dynamia.tools.web.actions.Action;
import com.dynamia.tools.web.actions.ActionEventBuilder;
import com.dynamia.tools.web.actions.ActionRenderer;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

/**
 *
 * @author mario_2
 */
public class SiteSelectorActionRenderer implements ActionRenderer<SiteSelector> {

    @Override
    public SiteSelector render(final Action action, final ActionEventBuilder evtBuilder) {
        final SiteSelector selector = new SiteSelector();
        selector.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                action.actionPerformed(evtBuilder.buildActionEvent(selector.getSelectedItem().getValue(), null));
            }
        });

        return selector;
    }

}