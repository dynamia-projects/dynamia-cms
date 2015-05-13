package com.dynamia.cms.admin.shopping.actions;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Messagebox;

import com.dynamia.cms.site.payment.PaymentForm;
import com.dynamia.cms.site.payment.PaymentGateway;
import com.dynamia.cms.site.payment.PaymentUtils;
import com.dynamia.cms.site.payment.domain.PaymentGatewayConfig;
import com.dynamia.cms.site.payment.domain.PaymentTransaction;
import com.dynamia.cms.site.payment.services.PaymentService;
import com.dynamia.tools.commons.ApplicableClass;
import com.dynamia.tools.web.actions.InstallAction;
import com.dynamia.tools.web.crud.AbstractCrudAction;
import com.dynamia.tools.web.crud.CrudActionEvent;
import com.dynamia.tools.web.crud.CrudState;

@InstallAction
public class TestGatewayAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service;

	public TestGatewayAction() {
		setName("Test");
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(PaymentGatewayConfig.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		PaymentGateway gateway = service.getDefaultGateway();
		PaymentTransaction tx = gateway.newTransaction("main");
		tx.setCurrency("COP");
		tx.setEmail("user@emails.com");
		tx.setAmount(new BigDecimal(1000));
		tx.setTaxes(BigDecimal.ZERO);
		tx.setTaxesBase(BigDecimal.ZERO);
		tx.setTest(true);

		PaymentForm form = gateway.createForm(tx);

		StringBuilder sb = new StringBuilder();
		sb.append("TX: " + tx.getUuid() + "\n");
		sb.append("FORM: " + form.getUrl() + "  " + form.getHttpMethod() + "\n");
		sb.append(PaymentUtils.mapToString(form.getParameters()));
		
		Messagebox.show(sb.toString());

	}

}
