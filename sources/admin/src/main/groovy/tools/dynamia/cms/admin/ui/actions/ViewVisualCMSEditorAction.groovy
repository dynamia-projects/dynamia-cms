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
