/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.templates.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import tools.dynamia.cms.templates.Template
import tools.dynamia.cms.templates.services.TemplateService
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex

/**
 *
 * @author Mario Serrano Leones
 */
class TemplateSelector extends Combobox {

    static {
        ComponentAliasIndex.instance.add(TemplateSelector.class)
    }

    TemplateSelector() {
        itemRenderer = new TemplateSelectorRenderer()
    }

    void init() {
        children.clear()
        readonly = true
        TemplateService service = Containers.get().findObject(TemplateService.class)
        List<Template> templates = service.installedTemplates
        if (templates != null && !templates.empty) {
            int i = 0
            for (Template template : templates) {
                Comboitem item = new Comboitem()
                item.parent = this
                try {
                    itemRenderer.render(item, template, i)
                } catch (Exception ex) {
                    ex.printStackTrace()
                }
                i++
            }
        }
    }

    @Override
    void setParent(Component parent) {
        super.setParent(parent)
        init()
    }

}
