/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.shopping.ui

import org.zkoss.zk.ui.Component
import org.zkoss.zul.Combobox
import org.zkoss.zul.Comboitem
import org.zkoss.zul.ComboitemRenderer
import tools.dynamia.cms.site.payment.PaymentGateway
import tools.dynamia.integration.Containers
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.util.ZKUtil

/**
 *
 * @author Mario Serrano Leones
 */
class PaymentGatewaySelector extends Combobox {

	static {
		ComponentAliasIndex.getInstance().add(PaymentGatewaySelector.class)
    }

    PaymentGatewaySelector() {
		setItemRenderer(new ComboitemRenderer<PaymentGateway>() {

			@Override
            void render(Comboitem item, PaymentGateway data, int index) throws Exception {
				item.setValue(data)
                item.setLabel(data.getName())
            }
		})
        init()
    }

    void init() {

		setReadonly(true)

        List<PaymentGateway> gateways = new ArrayList<>(Containers.get().findObjects(PaymentGateway.class))
        if (gateways != null && !gateways.isEmpty()) {

			ZKUtil.fillCombobox(this, gateways)
        }
	}

	@Override
    void setParent(Component parent) {
		super.setParent(parent) // To change body of generated methods, choose
									// Tools | Templates.
		init()
    }

}
