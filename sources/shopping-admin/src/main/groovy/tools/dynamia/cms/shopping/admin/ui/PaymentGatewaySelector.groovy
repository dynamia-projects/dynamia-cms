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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.shopping.admin.ui

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
