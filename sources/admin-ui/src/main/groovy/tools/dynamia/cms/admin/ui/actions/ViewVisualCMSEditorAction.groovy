/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.admin.ui.actions

import org.zkforge.ckez.CKeditor
import org.zkoss.zhtml.Form
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.ui.CMSeditor
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ViewVisualCMSEditorAction extends AbstractCMSEditorAction {

	ViewVisualCMSEditorAction() {
        name = "Visual Editor"
        image = "edit"
    }

	@Override
	void actionPerformed(ActionEvent evt) {
		CMSeditor editor = (CMSeditor) evt.source
		String content = (String) evt.data

		CKeditor ckeditor = new CKeditor()

        ckeditor.value = content
        ckeditor.hflex = "1"
        ckeditor.vflex = "1"
        ckeditor.filebrowserBrowseUrl = "resources"
        ckeditor.filebrowserUploadUrl = "resources"
        ckeditor.addEventListener("onSave",  {
			editor.value = ckeditor.value

            ckeditor.parent.parent.detach()
		})

		Form form = new Form()
		form.appendChild(ckeditor)
        form.style = "height: 100%"
        ZKUtil.showDialog("WYSIWYG Editor", form, "90%", "90%")

	}

	@Override
	ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
	}

}
