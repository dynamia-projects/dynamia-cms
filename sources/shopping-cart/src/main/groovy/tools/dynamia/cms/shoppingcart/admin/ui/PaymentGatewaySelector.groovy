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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.shoppingcart.admin.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import org.zkoss.zul.ComboitemRenderer
import tools.dynamia.cms.payment.PaymentGateway
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.util.ZKUtil

/**
 *
 * @author Mario Serrano Leones
 */
class PaymentGatewaySelector extends Combobox {

    static {
        ComponentAliasIndex.instance.add(PaymentGatewaySelector.class)
    }

    PaymentGatewaySelector() {
        itemRenderer = new ComboitemRenderer<PaymentGateway>() {

            @Override
            void render(Comboitem item, PaymentGateway data, int index) throws Exception {
                item.value = data
                item.label = data.name
            }
        }
        init()
    }

    void init() {

        readonly = true

        List<PaymentGateway> gateways = new ArrayList<>(Containers.get().findObjects(PaymentGateway.class))
        if (gateways != null && !gateways.isEmpty()) {

            ZKUtil.fillCombobox(this, gateways)
        }
    }

    @Override
    void setParent(Component parent) {
        super.setParent(parent)
        init()
    }

}
