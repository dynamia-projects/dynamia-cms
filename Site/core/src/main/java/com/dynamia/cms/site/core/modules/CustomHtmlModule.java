package com.dynamia.cms.site.core.modules;

import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;

@CMSModule
public class CustomHtmlModule extends AbstractModule {

	private static final String PARAM_CONTENT = "content";

	public CustomHtmlModule() {
		super("custom_html", "Custom Html", "core/modules/customhtml");
	}

	@Override
	public void init(ModuleContext context) {

		ModuleInstance instance = context.getModuleInstance();
		ModuleInstanceParameter content = instance.getParameter(PARAM_CONTENT);
		if (content != null && content.isEnabled()) {
			if (content.getExtra() != null && !content.getExtra().isEmpty()) {
				instance.addObject(PARAM_CONTENT, content.getExtra());
			} else {
				instance.addObject(PARAM_CONTENT, content.getValue());
			}
		}

	}

}
