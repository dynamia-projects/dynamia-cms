/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.admin.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import tools.dynamia.cms.core.api.TypeExtension
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex

/**
 *
 * @author Mario Serrano Leones
 */
class TypeSelector extends Combobox {

    private Class<? extends TypeExtension> typeExtensionClass = TypeExtension.class

    static {
        ComponentAliasIndex.instance.add(TypeSelector.class)
    }

    TypeSelector() {
        itemRenderer = new TypeSelectorRenderer()
    }

    Class<? extends TypeExtension> getTypeExtensionClass() {
        return typeExtensionClass
    }

    @SuppressWarnings("unchecked")
    void setTypeExtensionClass(String className) throws ClassNotFoundException {
        typeExtensionClass = (Class<? extends TypeExtension>) Class.forName(className)
    }

    void setTypeExtensionClass(Class<? extends TypeExtension> typeExtensionClass) {
        this.typeExtensionClass = typeExtensionClass
    }

    void init() {
        children.clear()
        readonly = true

        Collection<? extends TypeExtension> types = Containers.get().findObjects(typeExtensionClass)
        if (types != null && !types.empty) {
            int i = 0
            for (TypeExtension typeExtension : types) {
                Comboitem item = new Comboitem()
                item.parent = this
                try {
                    itemRenderer.render(item, typeExtension, i)
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
