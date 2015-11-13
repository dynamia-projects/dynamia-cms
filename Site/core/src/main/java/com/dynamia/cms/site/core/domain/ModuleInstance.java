/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.core.Orderable;

import tools.dynamia.domain.contraints.NotEmpty;

/**
 * @author mario
 */
@Entity
@Table(name = "cr_modules_instances")
public class ModuleInstance extends Content implements Orderable {

	@NotEmpty(message = "Module ID is required")
	@NotNull
	private String moduleId;
	@NotEmpty(message = "Select module position")
	private String position;
	@Column(name = "moduleAlias")
	private String alias;
	@NotEmpty(message = "Enter module title")
	private String title;
	private boolean enabled = true;
	private boolean titleVisible = true;

	private String styleClass;
	@OneToMany(mappedBy = "moduleInstance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ModuleInstanceParameter> parameters = new ArrayList<>();

	@Column(name = "instanceOrder")
	private int order;

	@Transient
	private Map<String, Object> model = new HashMap<String, Object>();

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public List<ModuleInstanceParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ModuleInstanceParameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isTitleVisible() {
		return titleVisible;
	}

	public void setTitleVisible(boolean titleVisible) {
		this.titleVisible = titleVisible;
	}

	@Override
	public String toString() {
		return getModuleId();
	}

	public String getParameterValue(String name) {
		ModuleInstanceParameter parameter = getParameter(name);
		if (parameter != null && parameter.isEnabled()) {
			return parameter.getValue();
		}

		return null;
	}

	public ModuleInstanceParameter getParameter(String name) {
		for (ModuleInstanceParameter parameter : parameters) {
			if (parameter.getName().equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void addObject(String name, Object object) {
		model.put(name, object);
	}

	public ModuleInstance clone() {
		ModuleInstance clone = new ModuleInstance();
		clone.alias = "";
		clone.enabled = false;
		clone.moduleId = moduleId;
		clone.order = order + 1;
		clone.position = position;
		clone.styleClass = styleClass;
		clone.title = title + "- copy";
		clone.titleVisible = titleVisible;

		for (ModuleInstanceParameter parameter : getParameters()) {
			ModuleInstanceParameter cloneParam = parameter.clone();
			clone.getParameters().add(cloneParam);
			cloneParam.setModuleInstance(clone);
		}

		return clone;
	}

}
