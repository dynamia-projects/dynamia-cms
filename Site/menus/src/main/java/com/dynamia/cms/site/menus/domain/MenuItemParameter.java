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
package com.dynamia.cms.site.menus.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.api.Parameter;

import tools.dynamia.domain.SimpleEntity;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "mn_menuitems_parameters")
public class MenuItemParameter extends SimpleEntity implements Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2483553006913604537L;
	@ManyToOne
	@NotNull
	private MenuItem menuItem;
	@Column(name = "paramName")
	@NotEmpty(message = "Enter parameter's name")
	private String name;
	@Column(name = "paramValue")
	@NotEmpty(message = "Enter parameter's value")
	private String value;
	private boolean enabled = true;
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String extra;

	public MenuItemParameter() {
		// TODO Auto-generated constructor stub
	}

	public MenuItemParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return value;
	}

	public MenuItemParameter clone() {
		MenuItemParameter clone = new MenuItemParameter();
		clone.enabled = enabled;
		clone.extra = extra;
		clone.name = name;
		clone.value = value;

		return clone;
	}
}
