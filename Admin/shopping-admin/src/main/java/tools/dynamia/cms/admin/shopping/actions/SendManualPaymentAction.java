package tools.dynamia.cms.admin.shopping.actions;

import org.springframework.beans.factory.annotation.Autowired;

import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.core.SiteContext;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.payment.api.Payment;
import tools.dynamia.cms.site.payment.services.PaymentService;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

@InstallAction
public class SendManualPaymentAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service;

	@Autowired
	private ShoppingCartService shoppingCartService;

	public SendManualPaymentAction() {
		setName("Send Payments");
	}

	@Override
	public CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ);
	}

	@Override
	public ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Payment.class);
	}

	@Override
	public void actionPerformed(CrudActionEvent evt) {
		Site site = SiteContext.get().getCurrent();
		ShoppingSiteConfig cfg = shoppingCartService.getConfiguration(site);

		if (cfg.getPaymentsSenderURL() != null && !cfg.getPaymentsSenderURL().isEmpty()) {
			service.sendManualPayments(site.getKey(), cfg.getPaymentsSenderURL(), cfg.getParametersAsMap());
			service.sendPayments(site.getKey(), cfg.getPaymentsSenderURL(), cfg.getParametersAsMap());
			UIMessages.showMessage("Sending... OK");
		} else {
			UIMessages.showMessage("Payments URL is not configured", MessageType.ERROR);
		}
		evt.getController().doQuery();
	}

}
