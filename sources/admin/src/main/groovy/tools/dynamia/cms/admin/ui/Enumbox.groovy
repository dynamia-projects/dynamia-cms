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
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.util.ZKUtil

/**
 *
 * @author Mario Serrano Leones
 */
class Enumbox extends Combobox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4436232527032479771L
    private Class<? extends Enum> enumClass
    private boolean valuesAsString

    static {
		ComponentAliasIndex.instance.add(Enumbox.class)
    }

    Enumbox() {
        readonly = true
    }

    Class<? extends Enum> getEnumClass() {
		return enumClass
    }

    void setEnumClass(Class<? extends Enum> enumClass) {
		this.enumClass = enumClass
    }

	@SuppressWarnings("unchecked")
    void setEnumClass(String className) throws ClassNotFoundException {
		enumClass = (Class<? extends Enum>) Class.forName(className)
    }

    void init() {
		Enum[] constants = enumClass.enumConstants
        if (valuesAsString) {
			List<String> names = new ArrayList<>()
            for (Enum c : constants) {
				names.add(c.name())
            }
			ZKUtil.fillCombobox(this, names, true)
        } else {
			ZKUtil.fillCombobox(this, constants, true)
        }

	}

	@Override
    void setParent(Component parent) {
        super.setParent(parent)
		init()
    }

    boolean isValuesAsString() {
		return valuesAsString
    }

    void setValuesAsString(boolean valuesAsString) {
		this.valuesAsString = valuesAsString
    }

}
