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

import org.zkoss.zul.*
import tools.dynamia.actions.Action
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionEventBuilder
import tools.dynamia.cms.shoppingcart.domain.ShoppingCartItem
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.viewers.util.Viewers
import tools.dynamia.zk.actions.ActionToolbar
import tools.dynamia.zk.viewers.form.FormView
import tools.dynamia.zk.viewers.table.TableView

@SuppressWarnings("unchecked")
class ShoppingOrderShipmentUI extends Borderlayout implements ActionEventBuilder {

	private ShoppingOrder shoppingOrder
    private ActionToolbar toolbar = new ActionToolbar(this)

    ShoppingOrderShipmentUI(ShoppingOrder order) {
		this.shoppingOrder = order
        createUI()
    }

	private void createUI() {
        hflex = "1"
        vflex = "1"

        new Center().parent = this
        center.autoscroll = true
        Div content = new Div()
        content.parent = center

        new South().parent = this
        toolbar.parent = south

        FormView<ShoppingOrder> orderFormView = (FormView<ShoppingOrder>) Viewers.getView(Viewers
				.findViewDescriptor("ShoppingOrderShipment"))
        orderFormView.readOnly = true

        TableView<ShoppingCartItem> itemTableView = (TableView<ShoppingCartItem>) Viewers.getView(ShoppingCartItem.class, "table",
                shoppingOrder.shoppingCart.items)
        itemTableView.height = "400px"
        itemTableView.vflex = false

        orderFormView.parent = content

        Groupbox title = new Groupbox()
        title.mold = "3d"
        title.open = false
        title.closable = false
        title.appendChild(new Caption("Order Details"))
        title.parent = content

        itemTableView.parent = content
    }

	@Override
    ActionEvent buildActionEvent(Object source, Map<String, Object> params) {
		return new ActionEvent(shoppingOrder, this)
    }

    void addAction(Action action) {
		toolbar.addAction(action)
    }

}
