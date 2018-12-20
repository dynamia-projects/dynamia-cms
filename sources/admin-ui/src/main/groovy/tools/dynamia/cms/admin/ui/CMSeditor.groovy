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

import org.zkforge.ckez.CKeditor
import org.zkoss.zk.ui.WrongValueException
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Borderlayout
import org.zkoss.zul.Center
import org.zkoss.zul.Div
import org.zkoss.zul.North
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.actions.ActionLoader
import tools.dynamia.actions.FastAction
import tools.dynamia.cms.admin.ui.actions.AbstractCMSEditorAction
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.BindingComponentIndex
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.actions.ActionToolbar
import tools.dynamia.zk.addons.Aceditor

class CMSeditor extends Div implements ActionEventBuilder {

    private Aceditor contentArea
    private ActionToolbar toolbar

    /**
     *
     */
    private static final long serialVersionUID = -4372806043096340598L

    static {
        ComponentAliasIndex.instance.add(CMSeditor.class)
        BindingComponentIndex.instance.put("value", CMSeditor.class)
        CKeditor.fileBrowserTemplate = "/cms-admin/browse"
        CKeditor.fileUploadHandlePage = "/cms-admin/browse"

    }

    CMSeditor() {
        sclass = "cms-editor"
        contentArea = new Aceditor()
        contentArea.width = "100%"
        contentArea.height = "100%"

        contentArea.mode = "html"
        contentArea.addEventListener(Events.ON_CHANGE, { e -> Events.postEvent(Events.ON_CHANGE, this, e) })
        toolbar = new ActionToolbar(this)

        Borderlayout layout = new Borderlayout()
        layout.appendChild(new North())
        layout.appendChild(new Center())

        layout.north.appendChild(toolbar)
        layout.center.appendChild(contentArea)
        layout.hflex = "1"
        layout.vflex = "1"
        appendChild(layout)

        loadActions()
    }

    private void loadActions() {
        toolbar.addAction(new FastAction("Save", "save", {
            Events.postEvent(new Event(Events.ON_CHANGE, this, contentArea.getValue()))
            UIMessages.showMessage("OK")
        }))



        ActionLoader<AbstractCMSEditorAction> loader = new ActionLoader<>(AbstractCMSEditorAction.class)
        loader.load().forEach { a -> toolbar.addAction(a) }

    }

    void setValue(String value) throws WrongValueException {
        contentArea.value = value
    }

    String getValue() {
        return contentArea.value
    }


    Aceditor getContentArea() {
        return contentArea
    }

    @Override
    ActionEvent buildActionEvent(Object arg0, Map<String, Object> arg1) {
        return new ActionEvent(value, this)
    }

}
