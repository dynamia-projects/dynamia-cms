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
import org.zkoss.zul.Messagebox
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import toosl.dynamia.cms.shoppingcart.api.ShoppingOrderSenderException

@InstallAction
class SendShoppingOrderAction extends AbstractCrudAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private CrudService crudService

    SendShoppingOrderAction() {
        name = "Send Order"
        description = "Send selected order to external system"
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
		ShoppingOrder order = (ShoppingOrder) evt.data
        if (order != null) {

			if (order.sended) {
				throw new ValidationError("Order " + order.number + " already was sended")
            }

			if (order.transaction.status != PaymentTransactionStatus.COMPLETED) {
				throw new ValidationError("Order is NOT completed")
            }

			UIMessages.showQuestion("Are you sure send order " + order.number + "?",  {
				try {
					service.sendOrder(order)
                    UIMessages.showMessage("Order " + order.number + " sended successfully")
                    evt.controller.doQuery()
                } catch (ShoppingOrderSenderException e) {
					Messagebox.show(e.message)
                } catch (ValidationError e) {
					UIMessages.showMessage(e.message, MessageType.WARNING)

                } catch (Exception e) {
					UIMessages.showMessage("Error sending order: " + e.message, MessageType.ERROR)
                    e.printStackTrace()
                }
			})
        }

	}

}
