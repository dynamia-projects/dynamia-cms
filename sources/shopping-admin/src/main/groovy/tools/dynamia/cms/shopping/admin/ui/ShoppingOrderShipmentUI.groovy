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
