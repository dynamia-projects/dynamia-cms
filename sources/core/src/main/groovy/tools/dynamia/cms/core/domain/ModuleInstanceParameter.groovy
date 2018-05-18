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
