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
package tools.dynamia.cms.admin.ui.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.core.Orderable
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.AbstractEntity
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

/**
 * Created by Mario on 18/11/2014.
 */
@InstallAction
class DecreaseOrderAction extends AbstractCrudAction {

	@Autowired
	private CrudService crudService

    DecreaseOrderAction() {
        name = "Move Down"
        image = "down"
        menuSupported = true
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Orderable orderable = (Orderable) evt.data
        if (orderable != null) {
            orderable.order = orderable.order + 1
            crudService.update(orderable)
            if (evt.controller != null) {
				evt.controller.doQuery()
                evt.controller.selected = (AbstractEntity) orderable
            }
		} else {
			UIMessages.showMessage("Select row", MessageType.WARNING)
        }
	}

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Orderable.class)
    }
}
