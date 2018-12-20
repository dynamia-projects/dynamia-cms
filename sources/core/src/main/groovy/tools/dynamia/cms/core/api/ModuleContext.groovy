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
