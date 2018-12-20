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

package tools.dynamia.cms.shopping.admin.ui

import org.zkoss.zul.Caption
import org.zkoss.zul.Div
import org.zkoss.zul.Groupbox
import tools.dynamia.cms.payment.domain.PaymentTransaction
import tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.viewers.form.FormView
import tools.dynamia.zk.viewers.table.TableView

class ShoppingOrderDetailsUI extends Div {

	private ShoppingOrder shoppingOrder

    ShoppingOrderDetailsUI(ShoppingOrder order) {
		this.shoppingOrder = order
        createUI()
    }

	private void createUI() {

		FormView<ShoppingOrder> orderFormView = (FormView<ShoppingOrder>) Viewers.getView(ShoppingOrder.class, "form", shoppingOrder)
        orderFormView.readOnly = true

        FormView<PaymentTransaction> txFormView = (FormView<PaymentTransaction>) Viewers.getView(PaymentTransaction.class, "form",
                shoppingOrder.transaction)
        txFormView.readOnly = true
        txFormView.vflex = "min"

        TableView<ShoppingCartItem> itemTableView = (TableView<ShoppingCartItem>) Viewers.getView(ShoppingCartItem.class, "table",
                shoppingOrder.shoppingCart.items)

        itemTableView.vflex = null
        itemTableView.height = "400px"

        orderFormView.parent = this

        Groupbox title = new Groupbox()
        title.mold = "3d"
        title.open = false
        title.closable = false
        title.appendChild(new Caption("Transaction Details"))
        title.parent = this

        txFormView.parent = this

        title = new Groupbox()
        title.mold = "3d"
        title.open = false
        title.closable = false
        title.appendChild(new Caption("Order Details"))
        title.parent = this

        itemTableView.parent = this
    }

}
