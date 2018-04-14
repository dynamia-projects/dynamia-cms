/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.admin.core.zk.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import tools.dynamia.cms.site.templates.Template
import tools.dynamia.cms.site.templates.services.TemplateService
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex

/**
 *
 * @author Mario Serrano Leones
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
