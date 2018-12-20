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
package tools.dynamia.cms.core.modules

import tools.dynamia.cms.core.api.AbstractModule
import tools.dynamia.cms.core.api.CMSModule
import tools.dynamia.cms.core.api.ModuleContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.ModuleInstanceParameter

@CMSModule
class CustomHtmlModule extends AbstractModule {

	private static final String PARAM_CONTENT = "content"
    private static final String PARAM_CONTENT_PLAIN = "contentPlain"
    private static final String PARAM_TEMPLATE_ENGINE = "templateEngine"

    CustomHtmlModule() {
		super("custom_html", "Custom Html", "core/modules/customhtml")
        description = "Allow you add custom html code"
        setVariablesNames(PARAM_TEMPLATE_ENGINE, PARAM_CONTENT)
    }

	@Override
    void init(ModuleContext context) {

		ModuleInstance instance = context.moduleInstance
        String contentText = "<b>CUSTOM MODULE NO CONTENT</b>"

        ModuleInstanceParameter content = instance.getParameter(PARAM_CONTENT)
        if (content != null && content.enabled) {
			if (content.extra != null && !content.extra.empty) {
				contentText = content.extra
            } else {
				contentText = content.value
            }
		}

		ModuleInstanceParameter contentPlain = instance.getParameter(PARAM_CONTENT_PLAIN)
        if (contentPlain != null && contentPlain.enabled) {
			if (contentPlain.extra != null && !contentPlain.extra.empty) {
				contentText = contentPlain.extra
            } else {
				contentText = contentPlain.value
            }
		}

		String templateEngine = context.getParameterValue(PARAM_TEMPLATE_ENGINE)

        instance.addObject(PARAM_TEMPLATE_ENGINE, templateEngine)

        instance.addObject(PARAM_CONTENT, contentText)
    }

}
