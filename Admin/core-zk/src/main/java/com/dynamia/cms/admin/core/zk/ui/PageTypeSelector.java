/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import com.dynamia.cms.site.pages.api.PageTypeExtension;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import java.util.Collection;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 *
 * @author mario
 */
public class PageTypeSelector extends Combobox {

    static {
        ComponentAliasIndex.getInstance().add(PageTypeSelector.class);
    }

    public PageTypeSelector() {
        setItemRenderer(new PageTypeSelectorRenderer());
    }

    public void init() {
        getChildren().clear();
        setReadonly(true);

        Collection<PageTypeExtension> types = Containers.get().findObjects(PageTypeExtension.class);
        if (types != null && !types.isEmpty()) {
            int i = 0;
            for (PageTypeExtension template : types) {
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
        super.setParent(parent); //To change body of generated methods, choose Tools | Templates.
        init();
    }

}
