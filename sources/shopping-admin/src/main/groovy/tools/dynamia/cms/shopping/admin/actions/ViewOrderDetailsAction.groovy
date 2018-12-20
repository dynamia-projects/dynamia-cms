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

package tools.dynamia.cms.shopping.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shopping.admin.ui.ShoppingOrderDetailsUI
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.services.CrudService
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.crud.actions.ViewDataAction
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ViewOrderDetailsAction extends ViewDataAction {

	@Autowired
	private CrudService crudService

    ViewOrderDetailsAction() {
        name = "Details"
        menuSupported = true
        image = "table"
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder shoppingOrder = (ShoppingOrder) evt.data
        if (shoppingOrder != null) {
			shoppingOrder = crudService.reload(shoppingOrder)

            view(shoppingOrder)
        }
	}

    void view(ShoppingOrder shoppingOrder) {
        ShoppingOrderDetailsUI ui = new ShoppingOrderDetailsUI(shoppingOrder)
        ui.vflex = "1"
        ui.style = "overflow: auto"
        ZKUtil.showDialog("Shopping Order No. " + shoppingOrder.number, ui, "90%", "90%")
    }

    @Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

}
