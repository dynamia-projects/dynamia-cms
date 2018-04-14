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
