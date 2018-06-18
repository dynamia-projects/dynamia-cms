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
package tools.dynamia.cms.core.api
/**
 *
 * @author Mario Serrano Leones
 */
class ModuleContext {

	private tools.dynamia.cms.core.domain.ModuleInstance moduleInstance
    private tools.dynamia.cms.core.domain.Site site

    ModuleContext(tools.dynamia.cms.core.domain.ModuleInstance moduleInstance) {
		super()
        this.moduleInstance = moduleInstance
        this.site = moduleInstance.site
    }

    tools.dynamia.cms.core.domain.ModuleInstance getModuleInstance() {
		return moduleInstance
    }

    tools.dynamia.cms.core.domain.ModuleInstanceParameter getParameter(String name) {
		return moduleInstance.getParameter(name)
    }

    String getParameterValue(String name, String defaultValue) {
		tools.dynamia.cms.core.domain.ModuleInstanceParameter param = getParameter(name)
        if (param != null) {
			return param.value
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

    tools.dynamia.cms.core.domain.Site getSite() {
		return site
    }

}