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
import org.zkoss.lang.Objects
import org.zkoss.zk.ui.WrongValueException
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.*
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.actions.ActionLoader
import tools.dynamia.cms.admin.ui.actions.AbstractCMSEditorAction
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.zk.BindingComponentIndex
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.actions.ActionToolbar

class CMSeditor extends Div implements ActionEventBuilder {

    private boolean unescape
    private boolean spellchek
    private Textbox contentArea
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
        contentArea = new Textbox()
        contentArea.width = "100%"
        contentArea.height = "100%"
        contentArea.setClientDataAttribute("ace-code-editor", "{theme:'ace/theme/eclipse', mode:'ace/mode/html'}")

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
        ActionLoader<AbstractCMSEditorAction> loader = new ActionLoader<>(AbstractCMSEditorAction.class)
        loader.load().forEach { a -> toolbar.addAction(a) }

    }

    void setValue(String value) throws WrongValueException {
        if (!Objects.equals(value, contentArea.value)) {
            contentArea.value = value
            contentArea.invalidate()
            Events.postEvent(new Event(Events.ON_CHANGE, this, value))
        }

    }

    String getValue() {
        if (!unescape) {
            return CMSUtil.escapeHtmlContent(contentArea.value)
        } else {
            return contentArea.value
        }
    }

    boolean isUnescape() {
        return unescape
    }

    void setUnescape(boolean unescape) {
        this.unescape = unescape
    }

    boolean isSpellchek() {
        return spellchek
    }

    void setSpellchek(boolean spellchek) {
        this.spellchek = spellchek
        contentArea.setDynamicProperty("spellcheck", spellchek)

    }

    Textbox getContentArea() {
        return contentArea
    }

    @Override
    ActionEvent buildActionEvent(Object arg0, Map<String, Object> arg1) {
        return new ActionEvent(value, this)
    }

}
