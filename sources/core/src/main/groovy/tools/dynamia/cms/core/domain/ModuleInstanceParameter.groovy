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
package tools.dynamia.cms.core.domain

import tools.dynamia.cms.core.api.Parameter
import tools.dynamia.domain.SimpleEntity

import javax.persistence.*

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_modules_instances_params")
class ModuleInstanceParameter extends SimpleEntity implements Parameter {

	@Column(name = "paramName")
	private String name
    @Column(name = "paramValue")
	private String value
    @Lob
	@Basic(fetch = FetchType.LAZY)
	private String extra
    private boolean enabled = true

    @ManyToOne
	private ModuleInstance moduleInstance

    ModuleInstanceParameter() {
	}

    ModuleInstanceParameter(String name, String value) {
		this.name = name
        setValue(value)

    }

    ModuleInstance getModuleInstance() {
		return moduleInstance
    }

    void setModuleInstance(ModuleInstance moduleInstance) {
		this.moduleInstance = moduleInstance
    }

    String getName() {
		return name
    }

    void setName(String paramName) {
		this.name = paramName
    }

    String getValue() {
		return value
    }

    void setValue(String value) {
		if (value != null && value.length() <= 255) {
			this.value = value
        } else {
			this.extra = value
        }

	}

    boolean isEnabled() {
		return enabled
    }

    void setEnabled(boolean enabled) {
		this.enabled = enabled
    }

    String getExtra() {
		return extra
    }

    void setExtra(String extra) {
		this.extra = extra
    }

	@Override
    String toString() {
		return name
    }

    ModuleInstanceParameter clone() {
		ModuleInstanceParameter clone = new ModuleInstanceParameter(name, value)
        clone.extra = extra
        clone.enabled = enabled
        return clone
    }

}
