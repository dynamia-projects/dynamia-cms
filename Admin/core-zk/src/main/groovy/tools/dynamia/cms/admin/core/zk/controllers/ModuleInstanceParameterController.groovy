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
package tools.dynamia.cms.admin.core.zk.controllers

import tools.dynamia.cms.site.core.domain.ModuleInstance
import tools.dynamia.cms.site.core.domain.ModuleInstanceParameter
import tools.dynamia.zk.crud.CrudController

class ModuleInstanceParameterController extends CrudController<ModuleInstanceParameter> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3698793765557086771L

    @Override
    void newEntity() {
		// TODO Auto-generated method stub
		super.newEntity()

        try {
			getEntity().setModuleInstance((ModuleInstance) getParameter("moduleInstance"))
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace()
        }
	}

}
