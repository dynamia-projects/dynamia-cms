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

import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.crud.actions.ViewDataAction

@InstallAction
class OrderPaymentDetailAction extends ViewDataAction {

	OrderPaymentDetailAction() {
        name = "Payment"
        menuSupported = true

    }

	@Override
    ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(ShoppingOrder.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		ShoppingOrder order = (ShoppingOrder) evt.data
        if (order != null) {
			super.actionPerformed(
					new CrudActionEvent(order.transaction, evt.source, evt.view, evt.controller))
        } else {
			UIMessages.showMessage("Select order", MessageType.WARNING)
        }
	}
}
