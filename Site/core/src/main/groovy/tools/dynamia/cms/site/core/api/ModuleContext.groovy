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
package tools.dynamia.cms.site.core.api

import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter
import tools.dynamia.cms.site.core.domain.Site

/**
 *
 * @author Mario Serrano Leones
 */
class ModuleContext {

	private ModuleInstance moduleInstance
    private Site site

    ModuleContext(ModuleInstance moduleInstance) {
		super()
        this.moduleInstance = moduleInstance
        this.site = moduleInstance.getSite()
    }

    ModuleInstance getModuleInstance() {
		return moduleInstance
    }

    ModuleInstanceParameter getParameter(String name) {
		return moduleInstance.getParameter(name)
    }

    String getParameterValue(String name, String defaultValue) {
		ModuleInstanceParameter param = getParameter(name)
        if (param != null) {
			return param.getValue()
        } else {
			return defaultValue
        }
	}

    String getParameterValue(String name) {
		return getParameterValue(name, null)
    }

    boolean isTrue(String parameterName) {
		return "true".equalsIgnoreCase(getParameterValue(parameterName, "false"))
    }

    Site getSite() {
		return site
    }

}
