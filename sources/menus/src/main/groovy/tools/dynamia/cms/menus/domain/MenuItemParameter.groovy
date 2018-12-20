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
