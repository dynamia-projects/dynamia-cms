/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import com.dynamia.cms.site.templates.Template;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

/**
 *
 * @author mario
 */
public class TemplateSelectorRenderer implements ComboitemRenderer<Template> {

    @Override
    public void render(Comboitem cmbtm, Template t, int i) throws Exception {
        cmbtm.setValue(t.getDirectoryName());
        cmbtm.setLabel(String.format("%s (%s)", t.getName(), t.getVersion()));
        cmbtm.setDescription(t.getDescription());
    }

}
