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
import org.zkoss.zul.Window
import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shopping.admin.ui.ShoppingOrderShipmentUI
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.commons.Messages
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.util.ZKUtil

@InstallAction
class ShipOrderAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private CrudService crudService

    ShipOrderAction() {
        name = "Ship Order"
        menuSupported = true
        image = "truck"
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
			try {
				shoppingOrder = crudService.reload(shoppingOrder)
                basicValidation(shoppingOrder)
                ShoppingOrderShipmentUI ui = new ShoppingOrderShipmentUI(shoppingOrder)
                ui.addAction(new ConfirmShipOrderAction())
                ZKUtil.showDialog("Ship Shopping Order No. " + shoppingOrder.number, ui, "90%", "90%")
            } catch (ValidationError e) {
				UIMessages.showMessage(e.message, MessageType.ERROR)
            }

		}
	}

	private void basicValidation(ShoppingOrder shoppingOrder) {

		if (!shoppingOrder.completed || !shoppingOrder.transaction.confirmed) {
			throw new ValidationError(Messages.get(ShoppingCartService.class, "OrderNotConfirmed", shoppingOrder.number))
        }

		if (shoppingOrder.shipped) {
			throw new ValidationError(Messages.get(ShoppingCartService.class, "OrderAlreadyShipped", shoppingOrder.number))
        }

	}

	class ConfirmShipOrderAction extends AbstractAction {

		ConfirmShipOrderAction() {
            name = "Confirm Shopping Order"
        }

		@Override
        void actionPerformed(ActionEvent evt) {
			try {
				ShoppingOrder shoppingOrder = (ShoppingOrder) evt.data
                service.shipOrder(shoppingOrder)
                ShoppingOrderShipmentUI ui = (ShoppingOrderShipmentUI) evt.source
                if (ui.parent instanceof Window) {
					ui.parent.detach()
                }
				UIMessages.showMessage(Messages.get(ShoppingCartService.class, "OrderShipedSuccesfull", shoppingOrder.number))
            } catch (ValidationError e) {
				UIMessages.showMessage(e.message, MessageType.ERROR)
            } catch (Exception e) {
				e.printStackTrace()
            }

		}
	}
}
