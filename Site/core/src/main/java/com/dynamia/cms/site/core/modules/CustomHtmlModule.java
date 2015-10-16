package com.dynamia.cms.site.core.modules;

import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.core.domain.ModuleInstanceParameter;

@CMSModule
public class CustomHtmlModule extends AbstractModule {

	private static final String PARAM_CONTENT = "content";
	private static final String PARAM_CONTENT_PLAIN = "contentPlain";

	public CustomHtmlModule() {
		super("custom_html", "Custom Html", "core/modules/customhtml");
		setDescription("Allow you add custom html code");
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

		ModuleInstanceParameter contentPlain = instance.getParameter(PARAM_CONTENT_PLAIN);
		if (contentPlain != null && contentPlain.isEnabled()) {
			if (content.getExtra() != null && !content.getExtra().isEmpty()) {
				instance.addObject(PARAM_CONTENT, contentPlain.getExtra());
			} else {
				instance.addObject(PARAM_CONTENT, contentPlain.getValue());
			}
		}

	}

}
