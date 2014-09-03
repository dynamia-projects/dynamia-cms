/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.dynamia.cms.site.core.api.TypeExtension;

/**
 *
 * @author mario
 */
public class TypeSelectorRenderer implements ComboitemRenderer<TypeExtension> {

	@Override
	public void render(Comboitem cmbtm, TypeExtension t, int i) throws Exception {
		cmbtm.setValue(t.getId());
		cmbtm.setLabel(t.getName());
		cmbtm.setDescription(t.getDescription());
	}

}
