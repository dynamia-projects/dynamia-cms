package tools.dynamia.cms.admin.shopping.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.payment.api.Payment
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
class SendManualPaymentAction extends AbstractCrudAction {

	@Autowired
	private PaymentService service

    @Autowired
	private ShoppingCartService shoppingCartService

    SendManualPaymentAction() {
        name = "Send Payments"
    }

	@Override
    CrudState[] getApplicableStates() {
		return CrudState.get(CrudState.READ)
    }

	@Override
    ApplicableClass[] getApplicableClasses() {
		return ApplicableClass.get(Payment.class)
    }

	@Override
    void actionPerformed(CrudActionEvent evt) {
		Site site = SiteContext.get().current
        ShoppingSiteConfig cfg = shoppingCartService.getConfiguration(site)

        if (cfg.paymentsSenderURL != null && !cfg.paymentsSenderURL.empty) {
			service.sendManualPayments(site.key, cfg.paymentsSenderURL, cfg.parametersAsMap)
            service.sendPayments(site.key, cfg.paymentsSenderURL, cfg.parametersAsMap)
            UIMessages.showMessage("Sending... OK")
        } else {
			UIMessages.showMessage("Payments URL is not configured", MessageType.ERROR)
        }
        evt.controller.doQuery()
    }

}
