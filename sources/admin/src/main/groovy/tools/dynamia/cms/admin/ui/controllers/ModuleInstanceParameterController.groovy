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
package tools.dynamia.cms.admin.ui.controllers

import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.core.domain.ModuleInstanceParameter
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
            entity.moduleInstance = (ModuleInstance) getParameter("moduleInstance")
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace()
        }
	}

}
