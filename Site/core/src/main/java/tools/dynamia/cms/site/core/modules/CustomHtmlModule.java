/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.core.modules;

import tools.dynamia.cms.site.core.api.AbstractModule;
import tools.dynamia.cms.site.core.api.CMSModule;
import tools.dynamia.cms.site.core.api.ModuleContext;
import tools.dynamia.cms.site.core.domain.ModuleInstance;
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter;

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
		instance.addObject(PARAM_CONTENT, "<b>CUSTOM MODULE NO CONTENT</b>");
		
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
			if (contentPlain.getExtra() != null && !contentPlain.getExtra().isEmpty()) {
				instance.addObject(PARAM_CONTENT, contentPlain.getExtra());
			} else {
				instance.addObject(PARAM_CONTENT, contentPlain.getValue());
			}
		}

	}

}
