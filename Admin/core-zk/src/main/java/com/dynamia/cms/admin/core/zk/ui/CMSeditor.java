package com.dynamia.cms.admin.core.zk.ui;

import org.zkforge.ckez.CKeditor;

import com.dynamia.tools.viewers.zk.BindingComponentIndex;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;

public class CMSeditor extends CKeditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4372806043096340598L;

	static {
		ComponentAliasIndex.getInstance().add(CMSeditor.class);
		BindingComponentIndex.getInstance().put("value", CMSeditor.class);
		setFileBrowserTemplate("browse");
		setFileUploadHandlePage("browse");
	}

	public CMSeditor() {
		setFilebrowserBrowseUrl("resources");
		setFilebrowserUploadUrl("resources");
		
		
	}

}
