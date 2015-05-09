/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.shopping.ui;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.viewers.zk.ComponentAliasIndex;
import com.dynamia.tools.web.util.ZKUtil;

/**
 *
 * @author mario
 */
public class PaymentGatewaySelector extends Combobox {

	static {
		ComponentAliasIndex.getInstance().add(PaymentGatewaySelector.class);
	}

	public PaymentGatewaySelector() {
		setItemRenderer(new ComboitemRenderer<PaymentGateway>() {

			@Override
			public void render(Comboitem item, PaymentGateway data, int index) throws Exception {
				item.setValue(data);
				item.setLabel(data.getName());
			}
		});
		init();
	}

	public void init() {

		setReadonly(true);

		List<PaymentGateway> gateways = new ArrayList<>(Containers.get().findObjects(PaymentGateway.class));
		if (gateways != null && !gateways.isEmpty()) {

			ZKUtil.fillCombobox(this, gateways);
		}
	}

	@Override
	public void setParent(Component parent) {
		super.setParent(parent); // To change body of generated methods, choose
									// Tools | Templates.
		init();
	}

}
