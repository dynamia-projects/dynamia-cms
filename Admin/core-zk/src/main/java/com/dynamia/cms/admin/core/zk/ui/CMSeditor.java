package com.dynamia.cms.admin.core.zk.ui;

import org.zkforge.ckez.CKeditor;

import com.dynamia.tools.viewers.zk.BindingComponentIndex;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;

public class CMSeditor extends CKeditor {

	static {
		ComponentAliasIndex.getInstance().add(CMSeditor.class);
		BindingComponentIndex.getInstance().put("value", CMSeditor.class);
	}

	public CMSeditor() {
		
	}

}
