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
