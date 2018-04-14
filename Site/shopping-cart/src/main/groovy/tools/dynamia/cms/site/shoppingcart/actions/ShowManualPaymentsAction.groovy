package tools.dynamia.cms.site.shoppingcart.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.payment.domain.ManualPayment
import tools.dynamia.cms.site.payment.services.PaymentService
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig
import tools.dynamia.cms.site.shoppingcart.services.ShoppingCartService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.api.UserProfile
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.services.UserService
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

		ModelAndView mv = evt.getModelAndView()
        mv.setViewName("payment/manuallist")

        mv.addObject("title", "Pagos Registrados")

        ShoppingSiteConfig cfg = service.getConfiguration(evt.getSite())
        String customer = (String) evt.getData()

        if (customer == null) {
			if (UserHolder.get().getCustomer() != null) {
				customer = UserHolder.get().getCustomer().getExternalRef()
            } else if (UserHolder.get().getCurrent().getProfile() == UserProfile.USER) {
				customer = UserHolder.get().getCurrent().getExternalRef()
            }
		}

		User user = userService.getByExternalRef(evt.getSite(), customer)
        if (user != null) {
			mv.addObject("title", "Pagos Registrados - " + user.getFullName())
            List<ManualPayment> payments = paymentService.findManualPaymentsByPayerId(evt.getSite().getKey(),
					user.getId().toString())
            BigDecimal total = BigDecimalUtils.sum("amount", payments)

            mv.addObject("payments", payments)
            mv.addObject("total", total)
            mv.addObject("customer", user)

            if (payments.isEmpty()) {
				CMSUtil.addSuccessMessage("No se encontraron pagos manuales registrados", mv)
            }
		}

	}

}
