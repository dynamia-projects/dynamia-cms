/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author mario
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
