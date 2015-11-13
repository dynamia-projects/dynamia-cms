/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import tools.dynamia.zk.ComponentAliasIndex;
import tools.dynamia.zk.util.ZKUtil;

/**
 *
 * @author mario
 */
public class Enumbox extends Combobox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4436232527032479771L;
	private Class<? extends Enum> enumClass;
	private boolean valuesAsString;

	static {
		ComponentAliasIndex.getInstance().add(Enumbox.class);
	}

	public Enumbox() {
		setReadonly(true);
	}

	public Class<? extends Enum> getEnumClass() {
		return enumClass;
	}

	public void setEnumClass(Class<? extends Enum> enumClass) {
		this.enumClass = enumClass;
	}

	@SuppressWarnings("unchecked")
	public void setEnumClass(String className) throws ClassNotFoundException {
		enumClass = (Class<? extends Enum>) Class.forName(className);
	}

	public void init() {
		Enum[] constants = enumClass.getEnumConstants();
		if (valuesAsString) {
			List<String> names = new ArrayList<>();
			for (Enum c : constants) {
				names.add(c.name());
			}
			ZKUtil.fillCombobox(this, names, true);
		} else {
			ZKUtil.fillCombobox(this, constants, true);
		}

	}

	@Override
	public void setParent(Component parent) {
		super.setParent(parent); // To change body of generated methods, choose
									// Tools | Templates.
		init();
	}

	public boolean isValuesAsString() {
		return valuesAsString;
	}

	public void setValuesAsString(boolean valuesAsString) {
		this.valuesAsString = valuesAsString;
	}

}
