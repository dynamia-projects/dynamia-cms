package tools.dynamia.cms.site.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.core.html.Option
import tools.dynamia.cms.site.payment.PaymentHolder
import tools.dynamia.cms.site.payment.domain.ManualPayment
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.User

@CMSAction
class NewPaymentAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Override
    String getName() {
		return "createNewPayment"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.modelAndView
        mv.viewName = "payment/new"
        mv.addObject("title", "Nuevo Pago Manual")
        ShoppingSiteConfig config = service.getConfiguration(evt.site)

        ManualPayment pay = new ManualPayment()
        pay.source = evt.site.key
        pay.registrator = UserHolder.get().fullName
        pay.registratorId = UserHolder.get().current.id.toString()
        pay.registratorCode = UserHolder.get().current.code

        User customer = UserHolder.get().customer
        if (customer == null) {
			CMSUtil.addErrorMessage("Seleccione cliente para registrar pago", mv)
            return
        }
        pay.payer = customer.fullName
        pay.payerCode = customer.code
        pay.payerId = customer.id.toString()

        loadPaymentTypes(mv, config)
        mv.addObject("payment", pay)

        PaymentHolder.get().currentManualPayment = pay

    }

	private void loadPaymentTypes(ModelAndView mv, ShoppingSiteConfig config) {
		String types = config.paymentTypes
        if (types != null) {
			mv.addObject("paymentTypes", Option.buildFromArray(types.split(","), null))
        }
	}

}
