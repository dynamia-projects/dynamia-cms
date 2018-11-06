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
package tools.dynamia.cms.admin.ui.ui

import org.zkforge.ckez.CKeditor
import org.zkoss.zk.ui.WrongValueException
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
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
        contentArea.theme = "eclipse"
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
