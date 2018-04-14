package tools.dynamia.cms.admin.core.zk.actions

import org.zkforge.ckez.CKeditor
import org.zkoss.zhtml.Form
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.core.zk.ui.CMSeditor
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ViewVisualCMSEditorAction extends AbstractCMSEditorAction {

	ViewVisualCMSEditorAction() {
		setName("Visual Editor")
		setImage("edit")
	}

	@Override
	void actionPerformed(ActionEvent evt) {
		CMSeditor editor = (CMSeditor) evt.getSource()
		String content = (String) evt.getData()

		CKeditor ckeditor = new CKeditor()

		ckeditor.setValue(content)
		ckeditor.setHflex("1")
		ckeditor.setVflex("1")
		ckeditor.setFilebrowserBrowseUrl("resources")
		ckeditor.setFilebrowserUploadUrl("resources")
		ckeditor.addEventListener("onSave",  {
			editor.setValue(ckeditor.getValue())

			ckeditor.getParent().getParent().detach()
		})

		Form form = new Form()
		form.appendChild(ckeditor)
		form.setStyle("height: 100%")
		ZKUtil.showDialog("WYSIWYG Editor", form, "90%", "90%")

	}

	@Override
	ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
	}

}
