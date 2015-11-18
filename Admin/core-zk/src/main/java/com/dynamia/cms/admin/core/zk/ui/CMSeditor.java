package com.dynamia.cms.admin.core.zk.ui;

import org.zkforge.ckez.CKeditor;

import tools.dynamia.zk.BindingComponentIndex;
import tools.dynamia.zk.ComponentAliasIndex;

public class CMSeditor extends CKeditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4372806043096340598L;

	static {
		ComponentAliasIndex.getInstance().add(CMSeditor.class);
		BindingComponentIndex.getInstance().put("value", CMSeditor.class);
		setFileBrowserTemplate("/browse");
		setFileUploadHandlePage("/browse");
		
	}

	public CMSeditor() {
		setFilebrowserBrowseUrl("resources");
		setFilebrowserUploadUrl("resources");
		
		
	}

}
