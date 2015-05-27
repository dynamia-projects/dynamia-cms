/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dynamia.cms.site.core.api.Parameter;
import com.dynamia.tools.domain.SimpleEntity;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cr_modules_instances_params")
public class ModuleInstanceParameter extends SimpleEntity implements Parameter {

	@Column(name = "paramName")
	private String name;
	@Column(name = "paramValue")
	private String value;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String extra;
	private boolean enabled = true;

	@ManyToOne
	private ModuleInstance moduleInstance;

	public ModuleInstanceParameter() {
	}

	public ModuleInstanceParameter(String name, String value) {
		this.name = name;
		setValue(value);

	}

	public ModuleInstance getModuleInstance() {
		return moduleInstance;
	}

	public void setModuleInstance(ModuleInstance moduleInstance) {
		this.moduleInstance = moduleInstance;
	}

	public String getName() {
		return name;
	}

	public void setName(String paramName) {
		this.name = paramName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && value.length() <= 255) {
			this.value = value;
		} else {
			this.extra = value;
		}

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return getName();
	}

	public ModuleInstanceParameter clone() {
		ModuleInstanceParameter clone = new ModuleInstanceParameter(name, value);
		clone.extra = extra;
		clone.enabled = enabled;
		return clone;
	}

}
