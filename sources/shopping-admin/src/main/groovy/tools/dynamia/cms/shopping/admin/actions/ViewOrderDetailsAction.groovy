/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
