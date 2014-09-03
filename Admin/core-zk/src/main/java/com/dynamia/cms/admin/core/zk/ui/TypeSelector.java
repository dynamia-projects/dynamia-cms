/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import java.util.Collection;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.dynamia.cms.site.core.api.TypeExtension;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;

/**
 *
 * @author mario
 */
public class TypeSelector extends Combobox {

	private Class<? extends TypeExtension> typeExtensionClass = TypeExtension.class;

	static {
		ComponentAliasIndex.getInstance().add(TypeSelector.class);
	}

	public TypeSelector() {
		setItemRenderer(new TypeSelectorRenderer());
	}

	public Class<? extends TypeExtension> getTypeExtensionClass() {
		return typeExtensionClass;
	}

	@SuppressWarnings("unchecked")
	public void setTypeExtensionClass(String className) throws ClassNotFoundException {
		typeExtensionClass = (Class<? extends TypeExtension>) Class.forName(className);
	}

	public void setTypeExtensionClass(Class<? extends TypeExtension> typeExtensionClass) {
		this.typeExtensionClass = typeExtensionClass;
	}

	public void init() {
		getChildren().clear();
		setReadonly(true);

		Collection<? extends TypeExtension> types = Containers.get().findObjects(getTypeExtensionClass());
		if (types != null && !types.isEmpty()) {
			int i = 0;
			for (TypeExtension template : types) {
				Comboitem item = new Comboitem();
				item.setParent(this);
				try {
					getItemRenderer().render(item, template, i);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				i++;
			}
		}
	}

	@Override
	public void setParent(Component parent) {
		super.setParent(parent); // To change body of generated methods, choose
									// Tools | Templates.
		init();
	}

}
