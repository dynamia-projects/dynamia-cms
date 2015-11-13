/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core.zk.ui;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.dynamia.cms.site.templates.Template;
import com.dynamia.cms.site.templates.services.TemplateService;

import tools.dynamia.integration.Containers;
import tools.dynamia.zk.ComponentAliasIndex;

/**
 *
 * @author mario
 */
public class TemplateSelector extends Combobox {

    static {
        ComponentAliasIndex.getInstance().add(TemplateSelector.class);
    }

    public TemplateSelector() {
        setItemRenderer(new TemplateSelectorRenderer());
    }

    public void init() {
        getChildren().clear();
        setReadonly(true);
        TemplateService service = Containers.get().findObject(TemplateService.class);
        List<Template> templates = service.getInstalledTemplates();
        if (templates != null && !templates.isEmpty()) {
            int i = 0;
            for (Template template : templates) {
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
