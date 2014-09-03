/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import org.zkoss.zul.Combobox;

import com.dynamia.cms.admin.core.SiteContext;
import com.dynamia.cms.site.templates.Template;
import com.dynamia.cms.site.templates.services.TemplateService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import com.dynamia.tools.web.util.ZKUtil;

/**
 *
 * @author mario
 */
public class PositionSelector extends Combobox {

	static {
		ComponentAliasIndex.getInstance().add(PositionSelector.class);
	}

	public PositionSelector() {
		init();
	}

	private void init() {
		getChildren().clear();
		setReadonly(true);
		TemplateService service = Containers.get().findObject(TemplateService.class);
		Template template = service.getTemplate(SiteContext.get().getCurrent());
		if (template != null) {
			ZKUtil.fillCombobox(this, template.getPositions());
		}

	}

}
