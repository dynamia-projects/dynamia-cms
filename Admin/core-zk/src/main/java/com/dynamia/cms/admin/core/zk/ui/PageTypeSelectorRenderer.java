/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import com.dynamia.cms.site.pages.api.PageTypeExtension;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

/**
 *
 * @author mario
 */
public class PageTypeSelectorRenderer implements ComboitemRenderer<PageTypeExtension> {

    @Override
    public void render(Comboitem cmbtm, PageTypeExtension t, int i) throws Exception {
        cmbtm.setValue(t.getId());
        cmbtm.setLabel(t.getName());
        cmbtm.setDescription(t.getDescription());
    }

}
