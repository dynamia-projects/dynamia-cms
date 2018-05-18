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
package tools.dynamia.cms.menus.domain

import tools.dynamia.cms.core.api.Parameter
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "mn_menuitems_parameters")
class MenuItemParameter extends SimpleEntity implements Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2483553006913604537L
	@ManyToOne
	@NotNull
	private MenuItem menuItem
	@Column(name = "paramName")
	@NotEmpty(message = "Enter parameter's name")
	private String name
	@Column(name = "paramValue")
	@NotEmpty(message = "Enter parameter's value")
	private String value
	private boolean enabled = true
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String extra

	MenuItemParameter() {
		// TODO Auto-generated constructor stub
	}

	MenuItemParameter(String name, String value) {
		this.name = name
		this.value = value
	}

	MenuItem getMenuItem() {
		return menuItem
	}

	void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem
	}

	String getName() {
		return name
	}

	void setName(String name) {
		this.name = name
	}

	String getValue() {
		return value
	}

	void setValue(String value) {
		this.value = value
	}

	String getExtra() {
		return extra
	}

	void setExtra(String extra) {
		this.extra = extra
	}

	boolean isEnabled() {
		return enabled
	}

	void setEnabled(boolean enabled) {
		this.enabled = enabled
	}

	@Override
	String toString() {
		return value
	}

	MenuItemParameter clone() {
		MenuItemParameter clone = new MenuItemParameter()
		clone.enabled = enabled
		clone.extra = extra
		clone.name = name
		clone.value = value

		return clone
	}
}
