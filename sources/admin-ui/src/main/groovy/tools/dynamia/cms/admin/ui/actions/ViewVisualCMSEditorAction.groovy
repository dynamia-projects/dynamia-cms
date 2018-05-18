package tools.dynamia.cms.admin.ui.actions

import org.zkforge.ckez.CKeditor
import org.zkoss.zhtml.Form
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.ui.ui.CMSeditor
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
