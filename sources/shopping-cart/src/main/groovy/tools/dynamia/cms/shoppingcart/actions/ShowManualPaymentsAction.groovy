package tools.dynamia.cms.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.payment.domain.ManualPayment
import tools.dynamia.cms.payment.services.PaymentService
import tools.dynamia.cms.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.users.UserHolder
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.services.UserService
import tools.dynamia.commons.BigDecimalUtils

@CMSAction
class ShowManualPaymentsAction implements SiteAction {

	@Autowired
	private ShoppingCartService service

    @Autowired
	private PaymentService paymentService

    @Autowired
	private UserService userService

    @Override
    String getName() {
		return "showManualPayments"
    }

	@Override
    void actionPerformed(ActionEvent evt) {

		ModelAndView mv = evt.modelAndView
        mv.viewName = "payment/manuallist"

        mv.addObject("title", "Pagos Registrados")

        ShoppingSiteConfig cfg = service.getConfiguration(evt.site)
        String customer = (String) evt.data

        if (customer == null) {
			if (UserHolder.get().customer != null) {
				customer = UserHolder.get().customer.externalRef
            } else if (UserHolder.get().current.profile == UserProfile.USER) {
				customer = UserHolder.get().current.externalRef
            }
		}

		User user = userService.getByExternalRef(evt.site, customer)
        if (user != null) {
			mv.addObject("title", "Pagos Registrados - " + user.fullName)
            List<ManualPayment> payments = paymentService.findManualPaymentsByPayerId(evt.site.key,
					user.id.toString())
            BigDecimal total = BigDecimalUtils.sum("amount", payments)

            mv.addObject("payments", payments)
            mv.addObject("total", total)
            mv.addObject("customer", user)

            if (payments.empty) {
				CMSUtil.addSuccessMessage("No se encontraron pagos manuales registrados", mv)
            }
		}

	}

}
